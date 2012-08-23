/*==========================================================================*\
 |  $Id: WCTreeSubcomponent.java,v 1.1 2011/05/13 19:43:46 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

package org.webcat.ui._base;

import org.webcat.ui.WCTable;
import org.webcat.ui.WCTree;
import org.webcat.ui.WCTreeModel;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import er.extensions.appserver.ERXDisplayGroup;

//-------------------------------------------------------------------------
/**
 * A class that can be extended by components that reside inside a WCTree,
 * and provides convenience methods for referring to that tree and its model.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: aallowat $
 * @version $Revision: 1.1 $, $Date: 2011/05/13 19:43:46 $
 */
public class WCTreeSubcomponent extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the WCTableSubComponent class.
     *
     * @param context the context
     */
    public WCTreeSubcomponent(WOContext context)
    {
        super(context);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the table that this subcomponent is nested inside.
     *
     * @return the table that contains this subcomponent
     */
    public WCTree tree()
    {
        return WCTree.currentTree();
    }


    // ----------------------------------------------------------
    /**
     * A convenience method to return the display group that is bound to the
     * table that contains this subcomponent.
     *
     * @return the display group that is bound to the table that contains this
     *     subcomponent
     */
    public WCTreeModel treeModel()
    {
        return tree().treeModel;
    }
}
