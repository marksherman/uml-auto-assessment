/*==========================================================================*\
 |  $Id: ReportDataSet.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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
 * Represents a data set in a report template.
 *
 * @author Tony Allevato
 * @version $Id: ReportDataSet.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportDataSet
    extends _ReportDataSet
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ReportDataSet object.
     */
    public ReportDataSet()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static ReportDataSet createNewReportDataSet(
            EOEditingContext ec,
            ReportTemplate reportTemplate,
            String entityName,
            String name,
            String description,
            int    referenceCount)
    {
        ReportDataSet dataSet = new ReportDataSet();
        ec.insertObject( dataSet );

        dataSet.setReportTemplateRelationship(reportTemplate);
        dataSet.setName(name);
        dataSet.setDescription(description);
        dataSet.setWcEntityName(entityName);
        dataSet.setReferenceCount(referenceCount);

        ec.saveChanges();

        return dataSet;
    }
}
