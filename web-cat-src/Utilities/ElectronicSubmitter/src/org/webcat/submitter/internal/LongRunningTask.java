/*==========================================================================*\
 |  $Id: LongRunningTask.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

import java.util.ArrayList;
import java.util.List;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.IProgressChangeListener;
import org.webcat.submitter.internal.utility.HierarchicalProgressTracker;

//--------------------------------------------------------------------------
/**
 * <p>
 * A partial implementation of {@link ILongRunningTask}.
 * </p><p>
 * Clients are not expected to extend this class. It is used internally to
 * provide common base functionality for progress notification on a task.
 * </p>
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public abstract class LongRunningTask implements ILongRunningTask
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new long running task with the specified description.
     *
     * @param description a description of this task
     */
    public LongRunningTask(String description)
    {
        this.description = description;
        this.tracker = new HierarchicalProgressTracker();

        progressChangeListeners = new ArrayList<IProgressChangeListener>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#beginSubtask(int)
     */
    public void beginSubtask(int totalWork)
    {
        tracker.beginTask(totalWork);
    }


    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#doWork(int)
     */
    public void doWork(int work)
    {
        tracker.worked(work);
        fireProgressChanged();
    }


    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#finishSubtask()
     */
    public void finishSubtask()
    {
        tracker.completeCurrentTask();
        fireProgressChanged();
    }


    // ----------------------------------------------------------
    /**
     * Notifies this task's listeners that the progress of the job has been
     * changed.
     */
    private void fireProgressChanged()
    {
        int progress = (int) (tracker.percentDone() * 100 + 0.5);

        if (progress != lastProgressValue)
        {
            for (IProgressChangeListener listener : progressChangeListeners)
            {
                listener.progressChanged(progress);
            }

            lastProgressValue = progress;
        }
    }


    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#addProgressChangeListener(IProgressChangeListener)
     */
    public void addProgressChangeListener(IProgressChangeListener listener)
    {
        if (listener != null && !progressChangeListeners.contains(listener))
        {
            progressChangeListeners.add(listener);
        }
    }


    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#removeProgressChangeListener(IProgressChangeListener)
     */
    public void removeProgressChangeListener(IProgressChangeListener listener)
    {
        progressChangeListeners.remove(listener);
    }


    // ----------------------------------------------------------
    /**
     * @see ILongRunningTask#getDescription()
     */
    public String getDescription()
    {
        return description;
    }


    //~ Static/instance variables .............................................

    /* The description of the task. */
    private String description;

    /* The internal progress tracking component. */
    private HierarchicalProgressTracker tracker;

    /* Used to prevent multiple notifications at the same progress value. */
    private int lastProgressValue;

    /* The list of listeners to be notified when the progress is updated. */
    private List<IProgressChangeListener> progressChangeListeners;
}
