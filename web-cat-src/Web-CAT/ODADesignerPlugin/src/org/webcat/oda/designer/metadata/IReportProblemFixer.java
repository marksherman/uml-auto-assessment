/*==========================================================================*\
 |  $Id: IReportProblemFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.metadata;

import org.eclipse.swt.widgets.Control;
import org.webcat.oda.commons.ReportModelProblem;

// ------------------------------------------------------------------------
/**
 * This interface defines methods that the report problem dialog uses to fix
 * problems in-place.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: IReportProblemFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public interface IReportProblemFixer
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the control that hosts the entire fixer user-interface (most likely
     * a composite).
     *
     * @return the control that hosts the fixer user-interface
     */
    Control getTopLevelFixerControl();

    // ----------------------------------------------------------
    /**
     * Sets the report model problem that the fixer is responsible for
     * correcting.
     *
     * @param problem
     *            the problem that this fixer is responsible for
     */
    void setReportModelProblem(ReportModelProblem problem);


    // ----------------------------------------------------------
    /**
     * Instructs the implementor that whatever fix that has been selected by the
     * user should now be applied to the model.
     */
    void applyFixToModel();
}
