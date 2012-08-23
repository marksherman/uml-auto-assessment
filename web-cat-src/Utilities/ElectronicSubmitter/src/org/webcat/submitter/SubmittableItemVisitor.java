/*==========================================================================*\
 |  $Id: SubmittableItemVisitor.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter;

import java.lang.reflect.InvocationTargetException;

//--------------------------------------------------------------------------
/**
 * Provides an interface for recursively (pre-order) visiting a forest of
 * submittable items.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public abstract class SubmittableItemVisitor
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Recursively visits the specified submittable item and its children.
     *
     * @param root the submittable item to visit
     * @throws InvocationTargetException
     */
    public void visit(ISubmittableItem root) throws InvocationTargetException
    {
        visit(new ISubmittableItem[] { root });
    }


    // ----------------------------------------------------------
    /**
     * Recursively visits each of the specified submittable items and their
     * children.
     *
     * @param roots the submittable items to visit
     * @throws InvocationTargetException
     */
    public void visit(ISubmittableItem[] roots)
    throws InvocationTargetException
    {
        for(ISubmittableItem item : roots)
        {
            accept(item);

            visit(item.getChildren());
        }
    }


    // ----------------------------------------------------------
    /**
     * Subclasses must implement this method which is called for each
     * submittable item as it is visited.
     *
     * @param item the submittable item being visited
     * @throws InvocationTargetException
     */
    protected abstract void accept(ISubmittableItem item)
    throws InvocationTargetException;
}
