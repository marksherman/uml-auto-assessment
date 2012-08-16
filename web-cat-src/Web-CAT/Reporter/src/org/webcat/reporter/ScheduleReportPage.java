/*==========================================================================*\
 |  $Id: ScheduleReportPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

//-------------------------------------------------------------------------
/**
 * A page that allows one to schedule reports and manage scheduled reports.
 *
 * @author Tony Allevato
 * @version $Id: ScheduleReportPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ScheduleReportPage
    extends WOComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     * @param context The page's context
     */
    public ScheduleReportPage(WOContext context)
    {
        super(context);
    }

}