/*==========================================================================*\
 |  $Id: PickTemplateToGeneratePage.java,v 1.3 2010/12/17 16:10:12 stedwar2 Exp $
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

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.foundation.NSArray;
import er.extensions.appserver.ERXDisplayGroup;

//-------------------------------------------------------------------------
/**
 * This page allows the user to select the template to use for a new report.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2010/12/17 16:10:12 $
 */
public class PickTemplateToGeneratePage
    extends ReporterComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public PickTemplateToGeneratePage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public ReportTemplate reportTemplate;
    public ERXDisplayGroup<ReportTemplate> reportTemplatesDisplayGroup;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public WODisplayGroup reportTemplatesDisplayGroup()
    {
        if (reportTemplatesDisplayGroup == null)
        {
            NSArray<ReportTemplate> templates =
                ReportTemplate.templatesAccessibleByUser(
                        localContext(), user());

            reportTemplatesDisplayGroup =
                new ERXDisplayGroup<ReportTemplate>();
            reportTemplatesDisplayGroup.setObjectArray(templates);
        }

        return reportTemplatesDisplayGroup;
    }


    // ----------------------------------------------------------
    public WOComponent templateChosen()
    {
        clearLocalReportState();
        setLocalReportTemplate(reportTemplate);

        return pageWithName(DescribeReportInputsPage.class);
    }
}
