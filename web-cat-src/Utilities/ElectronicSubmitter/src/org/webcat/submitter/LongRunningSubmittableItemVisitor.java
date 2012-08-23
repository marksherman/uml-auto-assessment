/*==========================================================================*\
 |  $Id: LongRunningSubmittableItemVisitor.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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
 * A visitor for submittable items that keeps track of its progress in a
 * long-running task.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public abstract class LongRunningSubmittableItemVisitor
extends SubmittableItemVisitor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new visitor with the specified long-running task.
     *
     * @param task the long-running task to use to track the progress of the
     *     visitor
     */
    public LongRunningSubmittableItemVisitor(ILongRunningTask task)
    {
        this.task = task;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Recursively visits each of the specified submittable items and their
     * children.
     *
     * @param roots the submittable items to visit
     * @throws InvocationTargetException
     */
    @Override
    public void visit(ISubmittableItem[] roots)
    throws InvocationTargetException
    {
        if (roots.length > 0)
        {
            task.beginSubtask(roots.length);

            super.visit(roots);

            task.finishSubtask();
        }
        else
        {
            task.doWork(1);
        }
    }


    //~ Static/instance variables .............................................

    /* The task that this visitor will run in. */
    private ILongRunningTask task;
}
