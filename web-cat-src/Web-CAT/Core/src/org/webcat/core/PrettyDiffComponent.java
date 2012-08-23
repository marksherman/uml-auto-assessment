/*==========================================================================*\
 |  $Id: PrettyDiffComponent.java,v 1.2 2012/03/28 13:48:08 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2012 Virginia Tech
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

package org.webcat.core;

import org.webcat.core.git.PrettyDiffResult;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

//-------------------------------------------------------------------------
/**
 * A component that displays a color-highlighted diff between two files.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2012/03/28 13:48:08 $
 */
public class PrettyDiffComponent extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new {@code PrettyDiffComponent} object.
     *
     * @param context the context
     */
    public PrettyDiffComponent(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    /** The diff results to display in the component. */
    public PrettyDiffResult diff;
}
