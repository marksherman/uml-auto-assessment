/*==========================================================================*\
 |  $Id: ManagedQueueDescriptor.java,v 1.5 2011/12/09 02:05:35 stedwar2 Exp $
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
import er.extensions.eof.ERXKey;

import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.webcat.core.IndependentEOManager;

// -------------------------------------------------------------------------
/**
 * A subclass of IndependentEOManager that holds one {@link QueueDescriptor}.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.5 $, $Date: 2011/12/09 02:05:35 $
 */
public class ManagedQueueDescriptor
    extends IndependentEOManager
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param descriptor the queue descriptor to wrap
     */
    public ManagedQueueDescriptor(QueueDescriptor descriptor)
    {
        super(descriptor);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>defaultJobProcessingTime</code> value.
     * @return the value of the attribute
     */
    public long defaultJobProcessingTime()
    {
        Number result = (Number)valueForKey(
            QueueDescriptor.DEFAULT_JOB_PROCESSING_TIME_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>jobsProcessed</code> value.
     * @return the value of the attribute
     */
    public long jobsProcessed()
    {
        Number result =
            (Number)valueForKey(QueueDescriptor.JOBS_PROCESSED_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>jobEntityName</code> value.
     * @return the value of the attribute
     */
    public String jobEntityName()
    {
        return (String)valueForKey(QueueDescriptor.JOB_ENTITY_NAME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>mostRecentJobWait</code> value.
     * @return the value of the attribute
     */
    public long mostRecentJobWait()
    {
        Number result =
            (Number)valueForKey(QueueDescriptor.MOST_RECENT_JOB_WAIT_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>newestEntryId</code> value.
     * @return the value of the attribute
     */
    public long newestEntryId()
    {
        Number result =
            (Number)valueForKey(QueueDescriptor.NEWEST_ENTRY_ID_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>requiresExclusiveHostAccess</code> value.
     * @return the value of the attribute
     */
    public boolean requiresExclusiveHostAccess()
    {
        Number result = (Number)valueForKey(
            QueueDescriptor.REQUIRES_EXCLUSIVE_HOST_ACCESS_KEY);
        return (result == null)
            ? false
            : (result.intValue() > 0);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>cumulativeProcessingTime</code> value.
     * @return the value of the attribute
     */
    public long cumulativeProcessingTime()
    {
        Number result =
            (Number)valueForKey(QueueDescriptor.CUMULATIVE_PROCESSING_TIME_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>movingAverageProcessingTime</code> value.
     * @return the value of the attribute
     */
    public long movingAverageProcessingTime()
    {
        Number result = (Number)valueForKey(
            QueueDescriptor.MOVING_AVERAGE_PROCESSING_TIME_KEY);
        return (result == null)
            ? 0L
            : result.longValue();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>workers</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    public NSArray workers()
    {
        return (NSArray)valueForKey(QueueDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>workers</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToWorkersRelationship(WorkerDescriptor value)
    {
        addObjectToBothSidesOfRelationshipWithKey(
            value, QueueDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>workers</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromWorkersRelationship(WorkerDescriptor value)
    {
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, QueueDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Increment all the necessary accumulation stats to indicate that
     * one more job has been processed.
     *
     * @param duration The time (in ms) taken to complete the job
     */
    public void addCompletedJobStats(long duration, long waitTime)
    {
        saveChanges();
        boolean saved = false;
        while (!saved)
        {
            takeValueForKey(
                waitTime, QueueDescriptor.MOST_RECENT_JOB_WAIT_KEY);
            takeValueForKey(
                1L + jobsProcessed(), QueueDescriptor.JOBS_PROCESSED_KEY);
            takeValueForKey(cumulativeProcessingTime() + duration,
                QueueDescriptor.CUMULATIVE_PROCESSING_TIME_KEY);

            // Calculate the exponential moving average

            // First, get the old value:
            long ema = movingAverageProcessingTime();
            if (ema == 0L)
            {
                // If we have no past data, first try to use the actual
                // long-term average as the initial EMA:
                ema = cumulativeProcessingTime();
                if (ema != 0L && jobsProcessed() > 0L)
                {
                    ema /= jobsProcessed();
                }
                else
                {
                    // If there is no data for a long-term average, just
                    // use the default value.
                    ema = defaultJobProcessingTime();
                }
            }

            if (ema == 0L)
            {
                // No past history at all, so just use first value we get
                ema = duration;
            }
            else
            {
                // We're using the formula for an exponential moving average
                // (EMA) from http://en.wikipedia.org/wiki/Rolling_average,
                // using the "S_t,alternate" and "EMA_today" formulae
                // in the section on Exponential Moving Average.
                //
                // EMA_new = EMA_old + alpha * (Duration - EMA_old)
                //
                // Here, alpha is a fraction between 0-1 that represents the
                // rate of decay in the exponential moving average.  The decay
                // factor we are using is the MOVING_AVERAGE_DECAY_FACTOR
                // in the QueueDescriptor class.  This decay factor
                // corresponds to 1/alpha, and roughly determines the "half
                // life" of data samples in the average.  We do the math in
                // floating point to ensure we get the right effects for small
                // durations (truncation wouldn't  significantly affect
                // results for larger numbers).

                ema = (long)(ema + ((double)duration - ema)
                    / QueueDescriptor.MOVING_AVERAGE_DECAY_FACTOR);
            }

            takeValueForKey(
                ema, QueueDescriptor.MOVING_AVERAGE_PROCESSING_TIME_KEY);

            if (tryToSaveChanges() == null)
            {
                saved = true;
            }
            else
            {
                refresh();
            }
        }
    }


    // ----------------------------------------------------------
    public void waitForNextJob()
    {
        EOEditingContext ec = clientContext();
        QueueDescriptor qd = (QueueDescriptor)localInstanceIn(ec);
        QueueDescriptor.waitForNextJob(qd);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( QueueDescriptor.class );
}
