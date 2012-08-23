/*==========================================================================*\
 |  $Id: GeneratedReportPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.eof.ERXConstant;
import java.io.File;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.json.JSONException;
import org.json.JSONObject;
import org.webcat.core.DeliverFile;
import org.webcat.core.MutableArray;
import org.webcat.core.MutableDictionary;
import org.webcat.grader.FinalReportPage;

//-------------------------------------------------------------------------
/**
 * This page displayed a generated report.
 *
 * @author  Tony Allevato
 * @version $Id: GeneratedReportPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class GeneratedReportPage
    extends ReporterComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public GeneratedReportPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    /** The associated refresh interval for this page */
    public int refreshTimeout = 15;
    public GeneratedReport generatedReport;
//    public Number reportGenerationJobId;
    public boolean wasCanceled = false;
    public int currentPageNumber = 0;
    public NSArray<String> resultSetsToExtract;
    public String resultSet;
    public int resultSetIndex;


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        // Get the generated report the first time we load the page, if it
        // exists. If it doesn't, then we'll continue trying to update it in
        // the long response delegate.

        generatedReport = localGeneratedReport();

        if (generatedReport.reportGenerationJob() != null)
        {
            currentPageNumber = 0;
        }
        else
        {
            // If the report has already been generated, start with page 1
            // instead of 0 (which is a placeholder that indicates nothing is
            // ready yet).
            currentPageNumber = 1;
        }

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public boolean isReportComplete()
    {
        if (generatedReport.isComplete())
        {
            return true;
        }
        else
        {
            Properties props = generatedReport.renderingProperties();
            return Boolean.valueOf(props.getProperty("isComplete", "false"));
        }
    }


    // ----------------------------------------------------------
    public String mainBlockTitle()
    {
        String prefix = "Your Report";
        return prefix + ": " + generatedReport.description();
    }


    // ----------------------------------------------------------
    public void setCurrentPageNumber(int pageNum)
    {
        currentPageNumber = pageNum;
    }


    // ----------------------------------------------------------
    public int highestPageSoFar()
    {
        Properties props = generatedReport.renderingProperties();
        return Integer.valueOf(props.getProperty(
                "highestRenderedPageNumber", "0"));
    }


    // ----------------------------------------------------------
    public synchronized JSONObject pollReportStatus()
    {
        JSONObject result = new JSONObject();

        try
        {
            if (wasCanceled)
            {
                result.put("isCanceled", true);
            }

            Properties props = generatedReport.renderingProperties();

            int highestPage = Integer.valueOf(props.getProperty(
                    "highestRenderedPageNumber", "0"));

            if (currentPageNumber == 0 && highestPage > 0)
            {
                // Kick off the first page once it's ready.
                currentPageNumber = 1;
            }

            ReportGenerationJob job = generatedReport.reportGenerationJob();

            if (job != null && job.worker() == null)
            {
                result.put("isStarted", false);
                result.put("queuePosition", 1); // FIXME get actual queue position
            }
            else
            {
                result.put("isStarted", true);
            }

            result.put("highestRenderedPageNumber", highestPage);

            result.put("isComplete",
                    generatedReport.isComplete() ||
                    Boolean.valueOf(props.getProperty(
                            "isComplete", "false")));

            MutableArray errors = generatedReport.errors();
            boolean hasErrors = (errors != null && errors.count() > 0);

            result.put("hasErrors", hasErrors);

            if (job == null)
            {
                result.put("progress", 100);
            }
            else
            {
                result.put("progress", (int) (job.progress() * 100 + 0.5));
            }
        }
        catch (JSONException e)
        {
            // Do nothing.
        }

        return result;
    }


    // ----------------------------------------------------------
    public String initialProgress()
    {
        ReportGenerationJob job = generatedReport.reportGenerationJob();
        if (job == null)
        {
            return "0%";
        }
        else
        {
            int progress = (int) (job.progress() * 100 + 0.5);
            return "" + progress + "%";
        }
    }


    // ----------------------------------------------------------
    public NSArray<?> resultSetsToExtract()
    {
        if (resultSetsToExtract == null)
        {
            NSMutableArray<String> resultSets = new NSMutableArray<String>();

            IReportDocument document = generatedReport.openReportDocument();
            IDataExtractionTask task =
                Reporter.getInstance().createDataExtractionTask(document);

            try
            {
                List<IResultSetItem> list = task.getResultSetList();

                for (IResultSetItem item : list)
                {
                    resultSets.addObject(item.getResultSetName());
                }
            }
            catch (EngineException e)
            {
                log.error("There was an error reading the result sets to be "
                        + "extracted from the report:", e);
            }

            document.close();

            resultSetsToExtract = resultSets;
        }

        return resultSetsToExtract;
    }


    // ----------------------------------------------------------
    public void cancelReport()
    {
        // FIXME does this work?
        ReportGenerationJob job = generatedReport.reportGenerationJob();
        job.setIsCancelled(true);
    }


    // ----------------------------------------------------------
    public NSDictionary<String, Object> generatedReportExtraErrorInfo()
    {
        if (generatedReport == null)
        {
            return null;
        }
        else
        {
            NSDictionary<String, Object> dict = (NSDictionary<String, Object>)
                generatedReport.errors().objectAtIndex(0);

            if ("extraInfo".equals(dict.objectForKey("entryKind")))
            {
                return dict;
            }
            else
            {
                return null;
            }
        }
    }


    // ----------------------------------------------------------
    public NSArray<MutableDictionary> generatedReportErrors()
    {
        if (generatedReport == null)
        {
            return null;
        }
        else
        {
            MutableArray _errors = generatedReport.errors();

            if (_errors != null)
            {
                if (_errors.count() == 0)
                {
                    return null;
                }

                MutableArray errors = new MutableArray(generatedReport.errors());

                if (errors.count() > 0)
                {
                    NSDictionary<String, Object> dict =
                        (NSDictionary<String, Object>) errors.objectAtIndex(0);

                    if ("extraInfo".equals(dict.objectForKey("entryKind")))
                    {
                        errors.removeObjectAtIndex(0);
                    }
                }

                return errors;
            }
            else
            {
                return null;
            }
        }
    }


    // ----------------------------------------------------------
    public NSArray<MutableDictionary> renderingErrors()
    {
        if (generatedReport == null)
        {
            return null;
        }
        else
        {
            return generatedReport.renderingErrors();
        }
    }


    // ----------------------------------------------------------
    private WOActionResults saveWithSaver(AbstractReportSaver saver)
    {
        DeliverFile file = (DeliverFile) pageWithName(
                DeliverFile.class.getName());

        Throwable error = saver.deliverTo(file);

        if (error != null)
        {
            ReportDownloadErrorPage page =
                pageWithName(ReportDownloadErrorPage.class);
            page.throwable = error;
            page.generatedReport = generatedReport;
            return page;
        }
        else
        {
            return file;
        }
    }


    // ----------------------------------------------------------
    public WOActionResults savePDF()
    {
        PDFReportSaver saver = new PDFReportSaver(generatedReport);
        return saveWithSaver(saver);
    }


    // ----------------------------------------------------------
    public WOActionResults saveExcel()
    {
        ExcelReportSaver saver = new ExcelReportSaver(generatedReport);
        return saveWithSaver(saver);
    }


    // ----------------------------------------------------------
    public WOActionResults saveZippedHTML()
    {
        HTMLReportSaver saver = new HTMLReportSaver(generatedReport);
        return saveWithSaver(saver);
    }


    // ----------------------------------------------------------
    public WOActionResults saveZippedCSV()
    {
        CSVReportSaver saver = new CSVReportSaver(generatedReport, null);
        return saveWithSaver(saver);
    }


    // ----------------------------------------------------------
    public WOActionResults saveOneCSV()
    {
        CSVReportSaver saver = new CSVReportSaver(generatedReport, resultSet);
        return saveWithSaver(saver);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( GeneratedReportPage.class );
}
