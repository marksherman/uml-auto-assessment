/*==========================================================================*\
 |  $Id: DefaultLongRunningTaskManager.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

package org.webcat.submitter.internal;

import java.lang.reflect.InvocationTargetException;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.ILongRunningTaskManager;

//--------------------------------------------------------------------------
/**
 * The default implementation of the long running task manager used by the
 * submitter if no other manager is specified. This implementation simply
 * executes tasks without any progress notification or visual feedback.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class DefaultLongRunningTaskManager implements ILongRunningTaskManager
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see ILongRunningTaskManager#run(ILongRunningTask)
     */
    public void run(ILongRunningTask runnable) throws InvocationTargetException
    {
        try
        {
            runnable.run();
        }
        catch (Exception e)
        {
            throw new InvocationTargetException(e);
        }
    }
}
