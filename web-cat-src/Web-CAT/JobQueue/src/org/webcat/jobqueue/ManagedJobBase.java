/*==========================================================================*\
 |  $Id: ManagedJobBase.java,v 1.2 2010/09/27 00:30:22 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008-2009 Virginia Tech
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

package org.webcat.jobqueue;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXConstant;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.webcat.core.IndependentEOManager;

// -------------------------------------------------------------------------
/**
 * A subclass of IndependentEOManager that holds one {@link JobBase}. This
 * class also provides progress management for a job; by placing these methods
 * on ManagedJobBase instead of {@link JobBase}, progress updates can be
 * persisted to the database immediately and independently of other objects in
 * various editing contexts.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:30:22 $
 */
public abstract class ManagedJobBase
    extends IndependentEOManager
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param job The job to wrap
     */
    public ManagedJobBase(JobBase job)
    {
        super(job);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>enqueueTime</code> value.
     * @return the value of the attribute
     */
    public NSTimestamp enqueueTime()
    {
        return (NSTimestamp)valueForKey(JobBase.ENQUEUE_TIME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>enqueueTime</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setEnqueueTime( NSTimestamp value )
    {
        takeValueForKey(value, JobBase.ENQUEUE_TIME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>isCancelled</code> value.
     * @return the value of the attribute
     */
    public boolean isCancelled()
    {
        return (Boolean)valueForKey(JobBase.IS_CANCELLED_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>isCancelled</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setIsCancelled( boolean value )
    {
        takeValueForKey(value, JobBase.IS_CANCELLED_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>isReady</code> value.
     * @return the value of the attribute
     */
    public boolean isReady()
    {
        return (Boolean)valueForKey(JobBase.IS_READY_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>isReady</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setIsReady( boolean value )
    {
        takeValueForKey(value, JobBase.IS_READY_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>priority</code> value.
     * @return the value of the attribute
     */
    public int priority()
    {
        Number result =
            (Number)valueForKey(JobBase.PRIORITY_KEY);
        return (result == null)
            ? 0
            : result.intValue();
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>priority</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setPriority(int value)
    {
        takeValueForKey(
            ERXConstant.integerForInt(value),
            JobBase.PRIORITY_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>progress</code> value.
     * @return the value of the attribute
     */
    public double progress()
    {
        Number result =
            (Number)valueForKey(JobBase.PROGRESS_KEY);
        return (result == null)
            ? 0
            : result.doubleValue();
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>progress</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setProgress(double value)
    {
        takeValueForKey(Double.valueOf(value), JobBase.PROGRESS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>progressMessage</code> value.
     * @return the value of the attribute
     */
    public String progressMessage()
    {
        return (String)valueForKey(JobBase.PROGRESS_MESSAGE_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>progressMessage</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setProgressMessage(String value)
    {
        takeValueForKey(value, JobBase.PROGRESS_MESSAGE_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>scheduledTime</code> value.
     * @return the value of the attribute
     */
    public NSTimestamp scheduledTime()
    {
        return (NSTimestamp)valueForKey(JobBase.SCHEDULED_TIME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>scheduledTime</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setScheduledTime(NSTimestamp value)
    {
        takeValueForKey(value, JobBase.SCHEDULED_TIME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the value of the <code>suspensionReason</code> attribute.
     *
     * @return the value of the attribute
     */
    public String suspensionReason()
    {
        return (String) valueForKey(JobBase.SUSPENSION_REASON_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Set the value of the <code>suspensionReason</code> attribute.
     *
     * @param value The new value of the attribute
     */
    public void setSuspensionReason(String value)
    {
        takeValueForKey(value, JobBase.SUSPENSION_REASON_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entity pointed to by the <code>user</code>
     * relationship.
     * @return the entity in the relationship
     */
    public org.webcat.core.User user()
    {
        return (org.webcat.core.User)valueForKey(JobBase.USER_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>user</code>
     * relationship.  This method is a type-safe version of
     * <code>addObjectToBothSidesOfRelationshipWithKey()</code>.
     *
     * @param value The new entity to relate to
     */
    public void setUserRelationship(org.webcat.core.User value)
    {
        addObjectToBothSidesOfRelationshipWithKey(value, JobBase.USER_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entity pointed to by the <code>worker</code>
     * relationship.
     * @return the entity in the relationship
     */
    public WorkerDescriptor worker()
    {
        return (WorkerDescriptor)valueForKey(JobBase.WORKER_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>worker</code>
     * relationship.  This method is a type-safe version of
     * <code>addObjectToBothSidesOfRelationshipWithKey()</code>.
     *
     * @param value The new entity to relate to
     */
    public void setWorkerRelationship(WorkerDescriptor value)
    {
        addObjectToBothSidesOfRelationshipWithKey(value, JobBase.WORKER_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Begins a task or subtask for progress monitoring.
     *
     * @param taskName a description of the task that will be stored in the
     *     <code>progressMessage</code> attribute of the job
     * @param work the amount of work to be done for this task
     */
    public void beginTask(String taskName, int work)
    {
        if (progress == null)
        {
            progress = new HierarchicalProgressTracker();
        }

        progress.beginTask(taskName, work);
        setProgressMessage(progress.descriptionOfCurrentTask());

        saveProgress();
    }


    // ----------------------------------------------------------
    /**
     * Begins a task or subtask for progress monitoring.
     *
     * @param taskName a description of the task that will be stored in the
     *     <code>progressMessage</code> attribute of the job
     * @param weights an array of integer weights that determine how each of
     *     the subtasks of this task will be weighted in the progress
     *     calculation
     */
    public void beginTask(String taskName, int[] weights)
    {
        if (progress == null)
        {
            progress = new HierarchicalProgressTracker();
        }

        progress.beginTask(taskName, weights);
        setProgressMessage(progress.descriptionOfCurrentTask());

        saveProgress();
    }


    // ----------------------------------------------------------
    /**
     * Notifies the job that <code>delta</code> units of work have been
     * performed in the current task. Calls can be made rapidly to this method;
     * it is self-throttling so that the database is only updated every few
     * seconds to prevent performance deficiencies.
     *
     * @param delta the number of units of work completed
     */
    public void worked(int delta)
    {
        progress.worked(delta);
        saveProgress();
    }


    // ----------------------------------------------------------
    /**
     * Notifies the current task that its work is completed. Future calls to
     * {@link #worked(int)} will apply to the parent task.
     *
     * It is required to call this method explicitly when a task's work is
     * completed. If a task requires 5 units of work and a sequence of call
     * that amount to <code>worked(5)</code> are made, this does <b>not</b>
     * cause that task to be completed.
     */
    public void completeCurrentTask()
    {
        progress.completeCurrentTask();
        setProgressMessage(progress.descriptionOfCurrentTask());

        saveProgress();
    }


    // ----------------------------------------------------------
    /**
     * Updates the percentage of job progress completed in the job EO, and
     * saves it to the database if enough time has passed since the last save.
     */
    private void saveProgress()
    {
        setProgress(progress.percentDone());

        long currentTime = System.currentTimeMillis();
        if (!isCancelled() &&
                currentTime >= timeOfLastProgressSave + PROGRESS_SAVE_DELAY)
        {
            saveChanges();

            timeOfLastProgressSave = System.currentTimeMillis();
        }
    }


    //~ Instance/static variables .............................................

    private HierarchicalProgressTracker progress;
    private long timeOfLastProgressSave;

    private static final long PROGRESS_SAVE_DELAY = 3000;

    static Logger log = Logger.getLogger( JobBase.class );
}
