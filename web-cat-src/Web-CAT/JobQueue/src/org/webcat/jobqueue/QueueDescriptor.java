/*==========================================================================*\
 |  $Id: QueueDescriptor.java,v 1.8 2011/12/25 21:18:24 stedwar2 Exp $
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

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.woextensions.WCEC;
import org.webcat.woextensions.WCFetchSpecification;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXDefaultEditingContextDelegate;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXS;

// -------------------------------------------------------------------------
/**
 * Represents and identifies a single database-backed queue of jobs that
 * descends from {@link JobBase}.  All queues that support parallel processing
 * over a cluster of servers should have a QueueDescriptor and be stored
 * in the database, which is the sole arbiter for concurrency control
 * among clustered servers.  All workers across an arbitrary number of
 * servers then operate on the same shared queue of jobs stored in the
 * database.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.8 $, $Date: 2011/12/25 21:18:24 $
 */
public class QueueDescriptor
    extends _QueueDescriptor
{
    /**
     * Determines the rate at which past data points in the (exponential)
     * moving average for job processing times "decay".  A value of 20
     * means the "half life" of the most recent job processing time is
     * approximately 20 jobs.  See the "S_t,alternate" and "EMA_today"
     * formulae under exponential moving averages on
     * http://en.wikipedia.org/wiki/Rolling_average.  The decay factor
     * is 1/alpha.
     */
    public static final double MOVING_AVERAGE_DECAY_FACTOR = 20.0;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new QueueDescriptor object.
     */
    public QueueDescriptor()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Registers a queue in the database, if it has not already been
     * registered, and returns the associated descriptor.
     * @param context The editing context to use.
     * @param theJobEntityName The name of the {@link JobBase} subclass used
     *        to hold the queue's contents in the database.
     * @return The registered descriptor.
     */
    public static QueueDescriptor descriptorFor(
        EOEditingContext context, String theJobEntityName)
    {
        QueueDescriptor result = (QueueDescriptor)JobQueue.registerDescriptor(
            context,
            QueueDescriptor.ENTITY_NAME,
            new NSDictionary<String, String>(
                theJobEntityName,
                QueueDescriptor.JOB_ENTITY_NAME_KEY),
            new NSDictionary<String, Long>(
                new Long(0),
                QueueDescriptor.NEWEST_ENTRY_ID_KEY));

        // Make sure a dispenser has been created for this queue
        dispenserFor(result);

        return result;
    }

    // ----------------------------------------------------------
    /**
     * Retrieve a managed descriptor for a given job queue, registering
     * the queue if necessary.
     * @param theJobEntityName The name of the {@link JobBase} subclass used
     *        to hold the queue's contents in the database.
     * @return The managed descriptor.
     */
    public static ManagedQueueDescriptor managedDescriptorFor(
        EOEditingContext context, String theJobEntityName)
    {
        return new ManagedQueueDescriptor(
            descriptorFor(context, theJobEntityName));
    }


    // ----------------------------------------------------------
    /**
     * Registers a queue in the database, if it has not already been
     * registered.
     * @param theJobEntityName The name of the {@link JobBase} subclass used
     *        to hold the queue's contents in the database.
     */
    public static void registerQueue(String theJobEntityName)
    {
        EOEditingContext ec = queueContext();
        synchronized (ec)
        {
            ec.lock();
            try
            {
                QueueDescriptor qd = descriptorFor(ec, theJobEntityName);
                if (!registeredDescriptors.contains(qd))
                {
                    registeredDescriptors.add(qd);
                }
                log.debug("registerQueue(): registered objects = " +
                    ec.registeredObjects());
            }
            finally
            {
                ec.unlock();
            }
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public int pendingJobCount(EOEditingContext ec)
    {
        EOFetchSpecification fetchSpec = new WCFetchSpecification<JobBase>(
            jobEntityName(),
            ERXQ.and(
                ERXQ.isFalse(JobBase.IS_CANCELLED_KEY),
                ERXQ.isTrue(JobBase.IS_READY_KEY)),
            null);
        NSArray<?> jobs = ec.objectsWithFetchSpecification(fetchSpec);
        if (log.isDebugEnabled())
        {
            log.debug("pendingJobCount(): " + jobs.count() + " "
                + jobEntityName() + " jobs are ready");
        }
        return jobs.count();
    }


    // ----------------------------------------------------------
    /* package */ static void waitForNextJob(QueueDescriptor descriptor)
    {
        TokenDispenser dispenser = dispenserFor(descriptor);
        int lockCount = 0;
        try
        {
            if (descriptor.editingContext() instanceof ERXEC)
            {
                ERXEC ec = (ERXEC)descriptor.editingContext();
                while (ec.lockCount() > 0)
                {
                    ec.unlock();
                    lockCount++;
                }
            }
            dispenser.getJobToken();
        }
        finally
        {
            while (lockCount > 0)
            {
                descriptor.editingContext().lock();
                lockCount--;
            }
        }
    }


    // ----------------------------------------------------------
    /* package */ static void newJobIsReadyOn(QueueDescriptor descriptor)
    {
        dispenserFor(descriptor).depositToken();
    }


    // ----------------------------------------------------------
    private static TokenDispenser dispenserFor(QueueDescriptor descriptor)
    {
        Number id = descriptor.id();
        assert id != null;
        TokenDispenser dispenser = null;
        synchronized (dispensers)
        {
            dispenser = dispensers.get(id);
            if (dispenser == null)
            {
                int initialTokenCount = 0;
                String name = null;
                EOEditingContext ec = WCEC.newEditingContext();
                try
                {
                    ec.lock();
                    QueueDescriptor qd = descriptor.localInstance(ec);
                    initialTokenCount = qd.pendingJobCount(ec);
                    name = qd.jobEntityName();
                }
                finally
                {
                    ec.unlock();
                    ec.dispose();
                }
                dispenser = new TokenDispenser(name, initialTokenCount);
                dispensers.put(id, dispenser);
            }
        }
        return dispenser;
    }


    // ----------------------------------------------------------
    // Used by QueueDelegate
    private static EOEditingContext queueContext()
    {
        return _ec;
    }


    // ----------------------------------------------------------
    /**
     * This class needs to be public as an implementation side-effect so that
     * WebObjects' NSSelector can access the delegate methods.
     */
    public static class QueueDelegate
        extends ERXDefaultEditingContextDelegate
    {
        // ----------------------------------------------------------
        public QueueDelegate()
        {
            // nothing to do
        }


        // ----------------------------------------------------------
        public void editingContextDidMergeChanges(EOEditingContext context)
        {
            if (log.isDebugEnabled())
            {
                log.debug(this + "editingContextDidMergeChanges(" + context
                    + ")");
            }
            if (jobContext == null)
            {
                jobContext = WCEC.newEditingContext();
            }
            synchronized (dispensers)
            {
                try
                {
                    jobContext.lock();
                    if (log.isDebugEnabled())
                    {
                        log.debug("dispenser map = " + dispensers);
                    }
                    for (Number id : dispensers.keySet())
                    {
                        QueueDescriptor descriptor =
                            forId(jobContext, id.intValue());
                        if (log.isDebugEnabled())
                        {
                            log.debug("Updating queue info for " +
                                descriptor.jobEntityName());
                        }
                        dispensers.get(id).ensureAtLeastNTokens(
                            descriptor.pendingJobCount(jobContext));
                    }
                }
                finally
                {
                    jobContext.unlock();
                }
            }
        }

        private EOEditingContext jobContext;
    }


    //~ Instance/static variables .............................................

    private static EOEditingContext _ec;
    public static class QueueEC extends WCEC
    {
        // ----------------------------------------------------------
        /**
         * Creates a new object.
         * @param os the parent object store
         */
        protected QueueEC(EOObjectStore os)
        {
            super(os);
        }


        // ----------------------------------------------------------
        @Override
        protected NSDictionary _objectBasedChangeInfoForGIDInfo(
            NSDictionary info)
        {
            NSDictionary result = super._objectBasedChangeInfoForGIDInfo(info);
            if (captureInfo)
            {
                captureInfo = false;
//                log.debug("_objectBasedChangeInfoForGIDInfo() = " + result);
                if (result != null)
                {
                    @SuppressWarnings("unchecked")
                    NSArray<EOEnterpriseObject> updates =
                        (NSArray<EOEnterpriseObject>)result
                        .valueForKey("updated");
                    if (updates != null && updates.count() > 0)
                    {
                        synchronized (dispensers)
                        {
                            for (EOEnterpriseObject eo : updates)
                            {
                                if (eo instanceof QueueDescriptor)
                                {
                                    QueueDescriptor descriptor =
                                        (QueueDescriptor)eo;
                                    log.debug(
                                        "QueueEC: Updating queue info for "
                                        + descriptor.jobEntityName());
                                    dispensers.get(descriptor.id())
                                        .ensureAtLeastNTokens(
                                            descriptor.pendingJobCount(this));
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        @Override
        public void _processObjectStoreChanges(NSDictionary arg0)
        {
            captureInfo = true;
            if (log.isDebugEnabled())
            {
                log.debug("QueueEC: _processObjectStoreChanges(): "
                    + arg0);
                try
                {
                    log.debug("    known = " + registeredObjects());
                }
                catch (Exception e)
                {
                    log.debug("    known = <exception printing list>");
                }
            }
            super._processObjectStoreChanges(arg0);
        }


        //~ Instance/static variables .........................................
        private boolean captureInfo = false;
    }


    static {
//        _ec = Application.newPeerEditingContext();
        _ec = new WCEC.WCECFactory() {
            protected EOEditingContext _createEditingContext(
                EOObjectStore parent)
            {
                return new QueueEC(parent == null
                    ? EOEditingContext.defaultParentObjectStore()
                    : parent);
            }
        }._newEditingContext();
//        _ec.setDelegate(new QueueDelegate());
    }

    // Accessed by inner QueueDelegate
    /* package */ static Map<Number, TokenDispenser> dispensers =
        new HashMap<Number, TokenDispenser>();

    // Keep all registered descriptors loaded in _ec in this array so
    // that the EC can detect changes to them, without them being
    // garbage-collected.
    private static final NSMutableArray<QueueDescriptor> registeredDescriptors
        = new NSMutableArray<QueueDescriptor>();

    static Logger log = Logger.getLogger(QueueDescriptor.class);
}
