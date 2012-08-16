/*==========================================================================*\
 |  $Id: Reporter.java,v 1.3 2011/12/25 21:18:25 stedwar2 Exp $
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

import java.util.Hashtable;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.webcat.birtruntime.BIRTRuntime;
import org.webcat.core.Subsystem;
import org.webcat.grader.EnqueuedJob;
import org.webcat.grader.Grader;
import org.webcat.jobqueue.QueueDescriptor;
import org.webcat.reporter.messaging.ReportCompleteMessage;
import org.webcat.woextensions.ECAction;
import org.webcat.woextensions.WCEC;
import static org.webcat.woextensions.ECAction.run;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import er.extensions.eof.ERXEOControlUtilities;

//-------------------------------------------------------------------------
/**
 * The primary class of the Reporter subsystem.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/12/25 21:18:25 $
 */
public class Reporter
    extends Subsystem
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new Reporter subsystem object.
     */
    public Reporter()
    {
        super();

        instance = this;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void init()
    {
        super.init();

        // Register the reporter subsystem's messages.

        ReportCompleteMessage.register();

        // Register the report generation job queue and create worker threads.

        QueueDescriptor.registerQueue(ReportGenerationJob.ENTITY_NAME);
    }


    // ----------------------------------------------------------
    public void start()
    {
        new ReportGenerationWorkerThread().start();

        // Create the global report design session used to gather information
        // about uploaded report templates.

        log.info("Creating global report design session");

        IDesignEngine designEngine =
            BIRTRuntime.getInstance().getDesignEngine();
        designSession = designEngine.newSessionHandle(null);
    }


    // ----------------------------------------------------------
    /**
     * Returns the sole instance of the reporter subsystem.
     *
     * @return the Reporter object that represents the subsystem.
     */
    public static Reporter getInstance()
    {
        return instance;
    }


    // ----------------------------------------------------------
    public IReportRunnable openReportTemplate(String path)
    {
        IReportEngine reportEngine =
            BIRTRuntime.getInstance().getReportEngine();

        try
        {
            return reportEngine.openReportDesign(path);
        }
        catch (EngineException e)
        {
            log.error("Error opening report template: " + path, e);
            return null;
        }
    }


    // ----------------------------------------------------------
    public IReportDocument openReportDocument(String path)
    {
        IReportEngine reportEngine =
            BIRTRuntime.getInstance().getReportEngine();

        try
        {
            return reportEngine.openReportDocument(path);
        }
        catch (EngineException e)
        {
            log.error("Error opening report template: " + path, e);
            return null;
        }
    }


    // ----------------------------------------------------------
    public IRunTask setupRunTaskForJob(ReportGenerationJob job)
    {
        IReportEngine reportEngine =
            BIRTRuntime.getInstance().getReportEngine();

        ReportTemplate template = job.generatedReport().reportTemplate();

        template.migrateTemplate();

        IReportRunnable runnable = openReportTemplate(template.filePath());
        IRunTask task = reportEngine.createRunTask(runnable);

        @SuppressWarnings("unchecked")
        Map<String, Object> appContext = task.getAppContext();
        if (appContext == null)
        {
            appContext = new Hashtable<String, Object>();
        }
        else
        {
            appContext = new Hashtable<String, Object>(appContext);
        }

        ManagedReportGenerationJob managedJob =
            new ManagedReportGenerationJob(job);
        OdaResultSetProvider resultProvider =
            new OdaResultSetProvider(managedJob);

        appContext.put("org.webcat.oda.resultSetProvider", resultProvider);

        task.setAppContext(appContext);

        return task;
    }


    // ----------------------------------------------------------
    public IGetParameterDefinitionTask createGetParameterDefinitionTask(
        IReportRunnable runnable)
    {
        IReportEngine reportEngine =
            BIRTRuntime.getInstance().getReportEngine();

        return reportEngine.createGetParameterDefinitionTask(runnable);
    }


    // ----------------------------------------------------------
    public IDataExtractionTask createDataExtractionTask(
        IReportDocument document)
    {
        IReportEngine reportEngine =
            BIRTRuntime.getInstance().getReportEngine();

        return reportEngine.createDataExtractionTask(document);
    }


    // ----------------------------------------------------------
    public SessionHandle designSession()
    {
        return designSession;
    }


    // ----------------------------------------------------------
    public boolean refreshThrottleStatus()
    {
        run(new ECAction(jobCountEC) { public void action() {
            jobCountAtLastThrottleCheck =
                ERXEOControlUtilities.objectCountWithQualifier(
                    ec, EnqueuedJob.ENTITY_NAME, READY_JOBS);
        }});

        /*if (log.isDebugEnabled())
        {
            log.debug("refreshThrottleStatus(): " + isThrottled());
        }*/

        return isThrottled();
    }


    // ----------------------------------------------------------
    public boolean isThrottled()
    {
        return jobCountAtLastThrottleCheck > 0;
    }


    // ----------------------------------------------------------
    public long throttleTime()
    {
        return Grader.getInstance().estimatedJobTime() *
            jobCountAtLastThrottleCheck;
    }


    //~ Instance/static variables .............................................

    private SessionHandle    designSession;
    private int              jobCountAtLastThrottleCheck;
    private EOEditingContext jobCountEC = WCEC.newEditingContext();

    /**
     * This is the sole instance of the reporter subsystem, initialized by the
     * constructor.
     */
    private static Reporter instance;

    private static final EOQualifier READY_JOBS =
        EnqueuedJob.discarded.isFalse().and(EnqueuedJob.paused.isFalse());

    private static Logger log = Logger.getLogger( Reporter.class );
}
