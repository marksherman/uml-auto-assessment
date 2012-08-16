/*==========================================================================*\
 |  $Id: PickReportToViewPage.java,v 1.3 2011/05/27 15:36:46 stedwar2 Exp $
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

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import er.extensions.appserver.ERXDisplayGroup;

//-------------------------------------------------------------------------
/**
 * This page allows the user to select among already-generated reports.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/05/27 15:36:46 $
 */
public class PickReportToViewPage
    extends ReporterComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public PickReportToViewPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public ERXDisplayGroup<GeneratedReport> generatedReportsDisplayGroup;
    public ERXDisplayGroup<ReportGenerationJob> enqueuedReportsDisplayGroup;

    public ReportGenerationJob reportJob;
    public GeneratedReport generatedReport;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        NSMutableDictionary<?, ?> bindings;

        bindings = generatedReportsDisplayGroup.queryBindings();
        bindings.setObjectForKey(user(), "user");
        generatedReportsDisplayGroup.fetch();

        bindings = enqueuedReportsDisplayGroup.queryBindings();
        bindings.setObjectForKey(user(), "user");
        enqueuedReportsDisplayGroup.fetch();

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public WOComponent viewReport()
    {
        if (generatedReport != null)
        {
            setLocalGeneratedReport(generatedReport);
            return pageWithName(GeneratedReportPage.class);
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public WOActionResults deleteSelectedReports()
    {
        NSArray<GeneratedReport> reports =
            generatedReportsDisplayGroup.selectedObjects();

        for (GeneratedReport report : reports)
        {
            localContext().deleteObject(report);
        }

        localContext().saveChanges();

        generatedReportsDisplayGroup.fetch();

        return null;
    }


    // ----------------------------------------------------------
    public WOComponent viewReportProgress()
    {
        GeneratedReport report = reportJob.generatedReport();

        if (report != null)
        {
            setLocalGeneratedReport(report);
            return pageWithName(GeneratedReportPage.class);
        }
        else
        {
            return null;
        }
    }
}
