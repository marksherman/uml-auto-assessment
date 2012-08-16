/*==========================================================================*\
 |  $Id: WorkerDescriptor.java,v 1.2 2010/09/27 00:30:22 stedwar2 Exp $
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.webcat.core.Application;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXConstant;
import er.extensions.foundation.ERXUtilities;
import er.extensions.foundation.ERXValueUtilities;

// -------------------------------------------------------------------------
/**
 * Represents and identifies a single {@link WorkerThread} on a single
 * Web-CAT host.  All worker threads that are going to run clustered
 * against a database-backed job queue should have a corresponding
 * WorkerDescriptor.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:30:22 $
 */
public class WorkerDescriptor
    extends _WorkerDescriptor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WorkerDescriptor object.
     */
    public WorkerDescriptor()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Look up the thread associated with a given descriptor on the current
     * host.
     * @param descriptor The descriptor to use.
     * @return The worker thread that corresponds to the given descriptor,
     * or null if there is not one on the current host.
     */
    public static WorkerThread threadFor(WorkerDescriptor descriptor)
    {
        return threads.get(descriptor.id());
    }


    // ----------------------------------------------------------
    /**
     * Registers a worker thread in the database, if it has not already been
     * registered.
     * @param context The editing context to use.
     * @param onHost The host on which this thread lives.
     * @param onQueue The queue on which this thread operates.
     * @return The registered descriptor.
     */
    /* package */ static WorkerDescriptor registerWorker(
        EOEditingContext context,
        HostDescriptor   onHost,
        QueueDescriptor  onQueue,
        WorkerThread     thread)
    {
        WorkerDescriptor result = (WorkerDescriptor)
            JobQueue.registerFirstAvailableDescriptor(
                context,
                ENTITY_NAME,
                new NSDictionary<String, Object>(
                    new Object[] { onHost, onQueue, ERXConstant.ZeroInteger },
                    new String[] { HOST_KEY, QUEUE_KEY, IS_ALIVE_KEY }
                    ),
                null,
                new NSDictionary<String, Object>(
                    ERXConstant.OneInteger, IS_ALIVE_KEY));
        threads.put(result.id(), thread);
        return result;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Determine whether this worker is currently blocked, because
     * another worker on the same host is currently processing a job
     * that requires exclusive host access.
     * @return True if this worker is currently blocked
     */
    public boolean isBlocked()
    {
        // TODO: implement this!
        return false;
    }


    // ----------------------------------------------------------
    /**
     * Look up the thread associated with this descriptor.  This method
     * can only be called if this descriptor is associated
     * with the current host, not a different host.
     * @return The worker thread that corresponds to this descriptor.
     */
    public WorkerThread thread()
    {
        return threadFor(this);
    }


    //~ Instance/static variables .............................................

    private static Map<Number, WorkerThread> threads =
        Collections.synchronizedMap(new HashMap<Number, WorkerThread>());

}
