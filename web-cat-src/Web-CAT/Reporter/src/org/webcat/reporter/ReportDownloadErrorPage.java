/*==========================================================================*\
 |  $Id: ReportDownloadErrorPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import java.io.PrintWriter;
import java.io.StringWriter;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

//------------------------------------------------------------------------
/**
 * Displays any errors that may have occurred when saving a report.
 *
 * @author Tony Allevato
 * @version $Id: ReportDownloadErrorPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportDownloadErrorPage extends ReporterComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public ReportDownloadErrorPage(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public Throwable throwable;
    public GeneratedReport generatedReport;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String mainBlockTitle()
    {
        return "Error Downloading Report: " + generatedReport.description();
    }


    // ----------------------------------------------------------
    public String throwableStackTrace()
    {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }


    // ----------------------------------------------------------
    public WOActionResults goBack()
    {
        setLocalGeneratedReport(generatedReport);
        GeneratedReportPage page = pageWithName(GeneratedReportPage.class);
        return page;
    }
}
