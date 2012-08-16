/*==========================================================================*\
 |  $Id: ReportDataSetQuery.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

// -------------------------------------------------------------------------
/**
 * For a report generation job or a generated report, this class maps a data set
 * in the report to a query that defines the result set to be used in the
 * report.
 *
 * @author Tony Allevato
 * @version $Id: ReportDataSetQuery.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportDataSetQuery
    extends _ReportDataSetQuery
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ReportDataSetQuery object.
     */
    public ReportDataSetQuery()
    {
        super();
    }


    //~ Methods ...............................................................

}
