/*==========================================================================*\
 |  $Id: JobBase.java,v 1.7 2012/01/05 19:49:57 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008-2011 Virginia Tech
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

import org.webcat.core.Application;
import org.webcat.woextensions.ECAction;
import static org.webcat.woextensions.ECAction.run;
import org.webcat.woextensions.WCEC;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXEOAccessUtilities;

// -------------------------------------------------------------------------
/**
 * This is the abstract base class for all "job" entities across all
 * Web-CAT subsystems.  It is designed to be used with EO-style horizontal
 * inheritance.  The corresponding EOModel definition provides the common
 * fields that all concrete subclasses will contain, although subclasses
 * can certainly add more as necessary.  To create a job subclass, create
 * an entity the normal way, then set its parent entity to JobBase.  To
 * generate SQL for the corresponding table, build off of the
 * {@link JobQueueDatabaseUpdates#createJobBaseTable(org.webcat.dbupdate.Database,String)}
 * method, which will generate the inherited field definitions for you.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.7 $, $Date: 2012/01/05 19:49:57 $
 */
public abstract class JobBase
    extends _JobBase
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new JobBase object.
     */
    public JobBase()
    {
        super();
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    @Override
    public void setProgress(double value)
    {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0.0)
        {
            value = 0.0;
        }
        else if (value > 1.0)
        {
            if (value <= 100.0)
            {
                value /= 100.0;
            }
            else
            {
                value = 1.0;
            }
        }
        super.setProgress(value);
    }


    // ----------------------------------------------------------
    /**
     * A convenience method to get the job's current progress as an integer
     * percentage.
     *
     * @return the percentage of job progress completed
     */
    public int progressPercentage()
    {
        return (int) (progress() * 100 + 0.5);
    }


    // ----------------------------------------------------------
    /**
     * Checks to see if this job is available for running, and if so,
     * allocates it to the given worker.  This sets the {@link #worker()}
     * relation to point to the worker thread on success, and returns true.
     * If the job is not available, it returns false.  Note that if the
     * method succeeds, the EC containing this job will have its changes
     * saved as part of the process, in order to commit the new value of
     * worker() to the database.
     *
     * @param withWorker The worker thread that wishes to take on this job
     * @return True if the worker has been allocated this job, or false
     *     if this worker cannot be given the job (because it has been
     *     cancelled or paused, or because it has already been allocated
     *     to another worker).
     */
    public boolean volunteerToRun(WorkerDescriptor withWorker)
    {
        if (isCancelled() || !isReady())
        {
            return false;
        }

        EOEditingContext ec = editingContext();
        try
        {
            if (ec != null && worker() == null)
            {
                setSuspensionReason(null);
                setWorkerRelationship(withWorker);
                ec.saveChanges();

                if (worker() == withWorker)
                {
                    workerThread = (WorkerThread) Thread.currentThread();
                    return true;
                }
            }
        }
        catch (EOGeneralAdaptorException e)
        {
            // assume optimistic locking failure
            if (ec != null)
            {
                ec.revert();
            }
        }
        return false;
    }


    // ----------------------------------------------------------
    /**
     * Overridden to indicate that the job queue needs to be notified that a
     * job that wasn't ready has now become ready when the changes are made to
     * the editing context.
     */
    @Override
    public void setIsReady(boolean value)
    {
        if (!isReady() && value)
        {
            log.debug("isReady for job " + id() + " transitioning to true; "
                    + "setting flag to notify queue on next save");

            queueNeedsNotified = true;
        }

        super.setIsReady(value);
    }


    // ----------------------------------------------------------
    private void notifyQueueOfReadyJob()
    {
        log.debug(entityName() + " queue descriptor increasing job count to "
                + "trigger job dispatch");

        queueNeedsNotified = false;

        run(new ECAction() { public void action() {
            QueueDescriptor queue = QueueDescriptor.descriptorFor(
                ec, entityName());

            boolean saved = false;
            while (!saved)
            {
                try
                {
                    queue.setNewestEntryId(id().longValue());
                    ec.saveChanges();
                    saved = true;
                }
                catch (EOGeneralAdaptorException e)
                {
                    if (ERXEOAccessUtilities.isOptimisticLockingFailure(e))
                    {
                        queue = (QueueDescriptor)
                            ERXEOAccessUtilities.refetchFailedObject(ec, e);
                    }
                }
            }

            log.debug(entityName() + " queue newest id now "
                + queue.newestEntryId());
        }});
    }


    // ----------------------------------------------------------
    @Override
    public void didInsert()
    {
        super.didInsert();

        if (queueNeedsNotified)
        {
            notifyQueueOfReadyJob();
        }
    }


    // ----------------------------------------------------------
    /**
     * Monitor the cancellation state of the job so that we can notify the
     * worker thread that the user wants to cancel this job.
     */
    @Override
    public void didUpdate()
    {
        super.didUpdate();

        if (!alreadyCancelled && isCancelled() && workerThread != null)
        {
            alreadyCancelled = true;
            workerThread.cancelJob();
            workerThread = null;
        }

        if (queueNeedsNotified)
        {
            notifyQueueOfReadyJob();
        }
    }


    // ----------------------------------------------------------
    public String toString()
    {
        return userPresentableDescription();
    }


    //~ Protected Methods .....................................................

    //~ Static/instance variables .............................................

    private transient boolean alreadyCancelled = false;
    private transient boolean queueNeedsNotified = false;
    private transient WorkerThread workerThread = null;
}
