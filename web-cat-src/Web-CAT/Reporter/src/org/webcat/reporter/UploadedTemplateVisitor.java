/*==========================================================================*\
 |  $Id: UploadedTemplateVisitor.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import java.util.List;
import org.webcat.oda.commons.DataSetMetadata;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSSet;

// ------------------------------------------------------------------------
/**
 * Visits the layout of a newly uploaded report template to collect information
 * about it such as the number of data set references and the types of visual
 * report elements that it contains.
 *
 * @author Tony Allevato
 * @version $Id: UploadedTemplateVisitor.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class UploadedTemplateVisitor extends DeepLayoutVisitor
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new {@link UploadedTemplateVisitor}.
     */
    public UploadedTemplateVisitor()
    {
        dataSetRefCounts = new NSMutableDictionary<DataSetHandle, Integer>();
        reportElements = new NSMutableSet<String>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void visitReportItem(ReportItemHandle handle)
    {
        // If the report item is using a data set and it is a Web-CAT ODA data
        // set, increment its reference count.

        DataSetHandle dataSet = handle.getDataSet();

        if (dataSet != null && DataSetMetadata.isWebCATDataSet(dataSet))
        {
            if (dataSetRefCounts.containsKey(dataSet))
            {
                int count = dataSetRefCounts.objectForKey(dataSet);
                count++;
                dataSetRefCounts.setObjectForKey(count, dataSet);
            }
            else
            {
                dataSetRefCounts.setObjectForKey(Integer.valueOf(1), dataSet);
            }
        }
    }


    // ----------------------------------------------------------
    @Override
    protected void visitTable(TableHandle handle)
    {
        // Since hidden tables have utility as a hack to form dependent data
        // sets, we only want to add a table to the design element set if it
        // is visible in some rendering.

        boolean alwaysHidden = false;

        List<HideRule> rules =
            handle.getListProperty(TableHandle.VISIBILITY_PROP);

        if (rules != null)
        {
            for(HideRule rule : rules)
            {
                if("all".equals(rule.getFormat()))
                {
                    if("true".equals(rule.getExpression()))
                    {
                        alwaysHidden = true;
                    }
                }
            }
        }

        if (!alwaysHidden)
        {
            reportElements.addObject(ReportTemplate.ELEMENT_TABLE);
        }

        // Make sure to call super so that the children get visited.
        super.visitTable(handle);
    }


    // ----------------------------------------------------------
    @Override
    protected void visitExtendedItem(ExtendedItemHandle handle)
    {
        String extension = handle.getExtensionName();

        if("Chart".equals(extension))
        {
            reportElements.addObject(ReportTemplate.ELEMENT_CHART);
        }
        else if("Crosstab".equals(extension))
        {
            reportElements.addObject(ReportTemplate.ELEMENT_CROSSTAB);
        }

        super.visitExtendedItem(handle);
    }


    // ----------------------------------------------------------
    /**
     * Gets a dictionary that contains all of the Web-CAT ODA data sets used in
     * the report template and the number of times that each is referenced in
     * the report.
     *
     * @return an {@link NSDictionary} containing the data set handles as keys
     *         and reference counts as values
     */
    public NSDictionary<DataSetHandle, Integer> dataSetsAndRefCounts()
    {
        return dataSetRefCounts;
    }


    // ----------------------------------------------------------
    /**
     * Gets the kinds of report elements that were found in the report. Examples
     * of "kinds" are "table", "crosstab", and "chart" (use the
     * {@link ELEMENT_}* constants found in {@link ReportTemplate}).
     *
     * @return an {@link NSSet} of strings that represent the kinds of report
     *         elements in the template
     */
    public NSSet<String> reportElements()
    {
        return reportElements;
    }


    //~ Static/instance variables .............................................

    private NSMutableDictionary<DataSetHandle, Integer> dataSetRefCounts;
    private NSMutableSet<String> reportElements;
}
