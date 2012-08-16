/*==========================================================================*\
 |  $Id: ILongRunningTaskManager.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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
 * Implemented by users of this library if they wish to provide custom
 * presentation of task progress.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public interface ILongRunningTaskManager
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called to tell the task manager to execute the specified task. The
     * implementation of this method should attach progress change listeners to
     * the task and then pass control to the task's
     * {@link ILongRunningTask#run()} method.
     *
     * @param task the task to execute
     * @throws InvocationTargetException if an exception was thrown by the
     *     task; the actual exception can be obtained by calling
     *     {@link InvocationTargetException#getCause()}
     */
    void run(ILongRunningTask task) throws InvocationTargetException;
}
