/*==========================================================================*\
 |  $Id: ReportGenerationWorkerThread.java,v 1.2 2011/12/25 21:18:25 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.webcat.core.MutableArray;
import org.webcat.jobqueue.WorkerThread;
import org.webcat.reporter.messaging.ReportCompleteMessage;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

//-------------------------------------------------------------------------
/**
 * A worker thread that generates reports using the BIRT reporting engine.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/12/25 21:18:25 $
 */
public class ReportGenerationWorkerThread extends
        WorkerThread<ReportGenerationJob>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the ReportGenerationWorkerThread class.
     */
    public ReportGenerationWorkerThread()
    {
        super(ReportGenerationJob.ENTITY_NAME);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called by the superclass to carry out the tasks for this job.
     */
    @Override
    protected void processJob()
    {
        ReportGenerationJob job = currentJob();
        GeneratedReport report = job.generatedReport();

        EOEditingContext ec = localContext();

        log.info("Processing report for: "
                + job.user().userName() + " (template: "
                + job.generatedReport().reportTemplate().name() + ")");

        ensureReportDirectoryExists(report);

        //int dataSetRefs = countDataSetReferences(job.reportTemplate());

        // Register the GeneratedReport object with the tracker so that the
        // progress page can observe it.
        //log.debug("Registering job with tracker");
        //ReportGenerationTracker.getInstance().startReportForJobId(
        //        job.id().intValue(), report.id().intValue(), dataSetRefs);

        runReport(report);

        if (isCancelling())
        {
            // Delete the GeneratedReport (and accompanying file) if it was
            // already created by this point.

            ec.deleteObject(report);
            ec.saveChanges();

            return;
        }

        storeErrorsInReportObject(report);

        report.setGeneratedTime(new NSTimestamp());
        report.setIsComplete(true);

        ec.saveChanges();

        // Send a notification that the report was completed.

        new ReportCompleteMessage(report).send();
    }


    // ----------------------------------------------------------
    /**
     * Ensures that the directory that will contain generated report documents
     * for this user exists.
     */
    private void ensureReportDirectoryExists(GeneratedReport report)
    {
        File reportDir = new File(report.generatedReportDir());

        if (!reportDir.exists())
        {
            reportDir.mkdirs();
        }
    }


    // ----------------------------------------------------------
    /**
     * Spawns the BIRT report engine task that runs the report.
     */
    private void runReport(GeneratedReport report)
    {
        ReportGenerationJob job = currentJob();
        String reportPath = report.generatedReportFile();
        List<Exception> exceptions = null;

        org.mozilla.javascript.Context.enter();

        try
        {
            log.debug("Generation thread: setting up run task");
            runTask = Reporter.getInstance().setupRunTaskForJob(job);
            runTask.setErrorHandlingOption(IEngineTask.CANCEL_ON_ERROR);

            IPageHandler pageHandler = ReportPageRenderer.getInstance()
                .createPageHandlerForReport(report);

            runTask.setPageHandler(pageHandler);

            log.debug("Generation thread: running BIRT reporting task");
            runTask.run(reportPath);

            exceptions = new ArrayList<Exception>(runTask.getErrors());
            if (exceptions.isEmpty())
            {
                exceptions = null;
            }

            runTask.close();
            runTask = null;
        }
        catch (EngineException e)
        {
            // Error creating process, so record it
            log.error("Exception generating " + reportPath, e);

            // This exception won't be thrown after the call to runTask.run,
            // so we don't need to worry about checking if exceptions is null
            // and clobbering anything already in there.

            exceptions = new ArrayList<Exception>();
            exceptions.add(0, e);
        }

        org.mozilla.javascript.Context.exit();

        // Copy the exceptions into a MutableArray that we can store in the
        // GeneratedReport EO, and log them to the console as well.

        errorsDuringRun = null;

        if (exceptions != null)
        {
            errorsDuringRun = new MutableArray();

            for (Exception ex : exceptions)
            {
                log.error("Error generating report:", ex);
                errorsDuringRun.addObject(ex);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Merges the list of errors produced by the run task with any errors that
     * were produced by the result sets and stores them as a MutableArray
     * inside the GeneratedReport object.
     */
    private void storeErrorsInReportObject(GeneratedReport report)
    {
        ReportGenerationJob job = currentJob();

        MutableArray errors = ReportExceptionTranslator.translateExceptions(
                errorsDuringRun);

        // Pull any additional error information that was generated in the
        // result set from the report tracker.

        /*NSDictionary<String, Object> _extraInfo =
                ReportGenerationTracker.getInstance().lastErrorInfoForJobId(
                        job.id().intValue());

        if (_extraInfo != null)
        {
            MutableDictionary extraInfo = new MutableDictionary(_extraInfo);
            extraInfo.setObjectForKey("extraInfo", "entryKind");
            errors.insertObjectAtIndex(extraInfo, 0);
        }*/

        report.setErrors(errors);
    }


    // ----------------------------------------------------------
    /**
     * Cancels the job by killing the BIRT report engine task.
     */
    @Override
    protected void cancelJob()
    {
        ReportGenerationJob job = currentJob();

        super.cancelJob();

        runTask.cancel();

        //ReportGenerationTracker.getInstance().removeReportIdForJobId(
        //        job.id().intValue());

        log.info("ReportGenerationJob with id " + job.id().intValue()
                + " was cancelled");
    }


    //~ Static/instance variables .............................................

    private IRunTask        runTask;
    private MutableArray    errorsDuringRun;

    static Logger log = Logger.getLogger(ReportGenerationWorkerThread.class);
}
