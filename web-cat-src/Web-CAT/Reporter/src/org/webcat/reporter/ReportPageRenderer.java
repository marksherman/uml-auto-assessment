/*==========================================================================*\
 |  $Id: ReportPageRenderer.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.webcat.birtruntime.BIRTRuntime;
import org.webcat.core.Application;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableDictionary;

//------------------------------------------------------------------------
/**
 * Manages the paginated rendering of reports. The
 * ReportGenerationQueueProcessor calls this when generating a report to
 * install a page handler that supports pagination. 
 *
 * @author Tony Allevato
 * @version $Id: ReportPageRenderer.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportPageRenderer
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the ReportPageRenderer class. 
     */
    private ReportPageRenderer()
    {
        engine = BIRTRuntime.getInstance().getReportEngine();
        handlers = new NSMutableDictionary<Number, PageHandler>();
    }
    
    
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the single instance of the ReportPageRenderer class.
     * 
     * @return the single instance of the ReportPageRenderer class
     */
    public synchronized static ReportPageRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new ReportPageRenderer();
        }
        
        return instance;
    }
    

    // ----------------------------------------------------------
    public boolean isPageRendered(GeneratedReport report, int pageNumber)
    {
        File file = new File(report.renderedHTMLPagePath(pageNumber));
        return file.exists();
    }


    // ----------------------------------------------------------
    public void renderPage(GeneratedReport report,
            IHTMLImageHandler imageHandler, int pageNumber)
    {
        try
        {
            IReportDocument document = report.openReportDocument();

            HTMLRenderOption option = new HTMLRenderOption();
            option.setEmbeddable(true);
            option.setImageHandler(imageHandler);
            option.setOutputFileName(report.renderedHTMLPagePath(
                    pageNumber));

            IRenderTask task = engine.createRenderTask(document);
            task.setPageNumber(pageNumber);
            task.setRenderOption(option);
            
            org.mozilla.javascript.Context.enter();
            task.render();
            org.mozilla.javascript.Context.exit();

            task.close();
            document.close();
        }
        catch (Exception e)
        {
            logError("There was an error rendering page "
                    + pageNumber + " for", report, e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a page handler for the specified report object. This is called
     * by the report generation queue processor when setting up the run-task
     * for the report.
     * 
     * @param report the report for which the page handler should be created
     * @return the page handler to assign to the run-task
     */
    public IPageHandler createPageHandlerForReport(GeneratedReport report)
    {
        PageHandler handler = new PageHandler(report);
        handlers.setObjectForKey(handler, report.id());
        
        return handler;
    }
    
    
    // ----------------------------------------------------------
    /**
     * Releases the page handler for the specified report object. This is
     * called by the page handler itself when it receives the notification that
     * rendering is complete.
     * 
     * @param report the report whose page handler should be released
     */
    private void releasePageHandlerForReport(GeneratedReport report)
    {
        handlers.removeObjectForKey(report.id());
    }


    // ----------------------------------------------------------
    private static void logError(String prefix, GeneratedReport report,
            Throwable t)
    {
        log.error(prefix + " the report \"" 
                + report.description() + "\" (id=" + report.id()
                + "): ", t);
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    /**
     * Implements the BIRT page handler interface to render each page of a
     * report 
     */
    private class PageHandler implements IPageHandler
    {
        //~ Constructor .......................................................

        // ----------------------------------------------------------
        /**
         * Initializes a new instance of the PageHandler class.
         * 
         * @param report the report associated with this generated report
         */
        public PageHandler(GeneratedReport report)
        {
            this.report = report;
            
            // Prepare the rendered resources directory before we do anything.

            report.prepareRenderingArea();
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        /**
         * Called when a new page of the report is ready to be rendered.
         * 
         * @param pageNumber the number of the page that is available
         * @param checkpoint true if the page is ready to be rendered
         * @param docInfo information about the report document
         */
        public synchronized void onPage(int readyPageNumber,
                boolean isReadyForViewing, IReportDocumentInfo docInfo)
        {
            // Here we update a properties file in the report's rendering
            // directory to indicate which pages are available for rendering.
            // It's the responsibility of the GeneratedReportPage component to
            // poll this (by using ReportPageRenderer methods above) and show
            // the user which pages are available and to display them when the
            // user requests.

            // NOTE: This function apparently does NOT get called for every
            // page in the report; instead, if batches of pages are ready at
            // once, this only gets called for the last page in the batch to
            // indicate that all pages up to that point are ready to be
            // rendered. So we don't actually bother rendering anything here --
            // instead, GeneratedReportPage will render each page as it needs
            // to be displayed to the user.

            if (isReadyForViewing)
            {
                synchronized (report)
                {
                    Properties props = report.renderingProperties();
                    props.setProperty("highestRenderedPageNumber",
                            Integer.toString(readyPageNumber));
                    props.setProperty("isComplete",
                            Boolean.toString(docInfo.isComplete()));
                    report.setRenderingProperties(props);
                    
                    if (docInfo.isComplete())
                    {
                        ReportPageRenderer.getInstance()
                            .releasePageHandlerForReport(report);
                    }
                }
            }
        }


        //~ Static/instance variables .........................................

        private GeneratedReport report;
    }


    //~ Static/instance variables .............................................

    private static ReportPageRenderer instance;
    private IReportEngine engine;
    private NSMutableDictionary<Number, PageHandler> handlers;
    
    private static final Logger log =
        Logger.getLogger(ReportPageRenderer.class);
}
