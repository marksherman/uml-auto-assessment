/*==========================================================================*\
 |  $Id: DeepLayoutVisitor.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

// ------------------------------------------------------------------------
/**
 * A subclass of the standard BIRT DesignVisitor that performs a deep traversal
 * of the report body layout by automatically visiting the children of each
 * element that it encounters, when applied initially to a
 * {@link ReportDesignHandle}.
 *
 * @author Tony Allevato
 * @version $Id: DeepLayoutVisitor.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class DeepLayoutVisitor extends DesignVisitor
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void visitCell(CellHandle handle)
    {
        super.visitCell(handle);

        applyToSlot(handle.getContent());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitGroup(GroupHandle handle)
    {
        super.visitGroup(handle);

        applyToSlot(handle.getHeader());
        applyToSlot(handle.getFooter());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitFreeForm(FreeFormHandle handle)
    {
        super.visitFreeForm(handle);

        applyToSlot(handle.getReportItems());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitGrid(GridHandle handle)
    {
        super.visitGrid(handle);

        applyToSlot(handle.getColumns());
        applyToSlot(handle.getRows());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitList(ListHandle handle)
    {
        super.visitList(handle);

        applyToSlot(handle.getHeader());
        applyToSlot(handle.getGroups());
        applyToSlot(handle.getDetail());
        applyToSlot(handle.getFooter());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitReportDesign(ReportDesignHandle handle)
    {
        super.visitReportDesign(handle);

        applyToSlot(handle.getBody());
    }


    // ----------------------------------------------------------
    @Override
    protected void visitRow(RowHandle handle)
    {
        super.visitRow(handle);

        applyToSlot(handle.getCells());
    }


    // ----------------------------------------------------------
    /**
     * Iterates over the elements in a slot and applies the visitor to each of
     * them in order.
     *
     * @param slot the slot to iterate over
     */
    private void applyToSlot(SlotHandle slot)
    {
        for(int i = 0; i < slot.getCount(); i++)
        {
            apply(slot.get(i));
        }
    }
}
