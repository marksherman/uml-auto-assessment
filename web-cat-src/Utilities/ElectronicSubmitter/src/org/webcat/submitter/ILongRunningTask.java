/*==========================================================================*\
 |  $Id: ILongRunningTask.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

//--------------------------------------------------------------------------
/**
 * <p>
 * Represents a potentially long-running task in the submitter, such as loading
 * the submission target definitions or submitting a set of files. The task can
 * notify listeners of its progress.
 * </p><p>
 * This interface is not intended to be implemented by clients. Custom
 * implementors of {@link ILongRunningTaskManager} will be passed objects that
 * implement this interface, to which they should attach progress change
 * listeners and then call the task's {@link #run()} method to execute it.
 * </p>
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public interface ILongRunningTask
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Contains the logic for the task. Called by
     * {@link ILongRunningTaskManager#run(ILongRunningTask)}.
     *
     * @throws Exception if any exception occurs; the task manager should catch
     *     this and then rethrow it wrapped by an
     *     {@link java.lang.reflect.InvocationTargetException}
     */
    void run() throws Exception;


    // ----------------------------------------------------------
    /**
     * Adds a progress change listener to this task.
     *
     * @param listener the listener to add
     */
    void addProgressChangeListener(IProgressChangeListener listener);


    // ----------------------------------------------------------
    /**
     * Removes a progress change listener from this task.
     *
     * @param listener the listener to remove
     */
    void removeProgressChangeListener(IProgressChangeListener listener);


    // ----------------------------------------------------------
    /**
     * Subdivides the next unit of work into a subtask that requires the
     * specified amount of work.
     *
     * @param totalWork the amount of work to subdivide the next unit into
     */
    void beginSubtask(int totalWork);


    // ----------------------------------------------------------
    /**
     * Performs the specified amount of work on the current subtask.
     *
     * @param work the amount of work to perform
     */
    void doWork(int work);


    // ----------------------------------------------------------
    /**
     * Completes the current subtask.
     */
    void finishSubtask();


    // ----------------------------------------------------------
    /**
     * Gets a text description of the task.
     *
     * @return a string giving a brief description of the task
     */
    String getDescription();
}
