/*==========================================================================*\
 |  $Id: ReportPageContent.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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
import java.io.IOException;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.IReportDocument;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;

//-------------------------------------------------------------------------
/**
 * A component that displays the contents of a page in a rendered report.
 *
 * @author Tony Allevato
 * @version $Id: ReportPageContent.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportPageContent extends WOComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public ReportPageContent(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public GeneratedReport report;
    public int pageNumber;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        try
        {
            // Render the page on demand if it hasn't already been rendered.

            ReportPageRenderer renderer = ReportPageRenderer.getInstance();
            if (!renderer.isPageRendered(report, pageNumber))
            {
                String baseURL = context.directActionURLForActionNamed(
                        "reportResource/image", null);
                
                DirectActionHTMLImageHandler imageHandler =
                    new DirectActionHTMLImageHandler(report, baseURL);

                IReportDocument document = report.openReportDocument();
                renderer.renderPage(report, imageHandler, pageNumber);
                document.close();
            }
            
            // Dump the contents of the report page out to the response.

            String htmlPath = report.renderedHTMLPagePath(pageNumber);
            File htmlFile = new File(htmlPath);

            NSData htmlData = new NSData(
                new FileInputStream(htmlFile), (int)htmlFile.length());
            response.appendContentData(htmlData);
        }
        catch (IOException e)
        {
            log.error("An error occurred while reading the rendered report", e);

            response.appendContentString("<p>There was an error reading the " +
                    "rendered report contents.</p>");
        }

        super.appendToResponse(response, context);
    }


    //~ Instance/static variables .............................................

    private static Logger log = Logger.getLogger(ReportPageContent.class);
}