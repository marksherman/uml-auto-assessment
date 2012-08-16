/*==========================================================================*\
 |  $Id: PreviewingResultCache.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

package org.webcat.oda.designer.preview;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.eclipse.core.runtime.jobs.Job;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewingResultCache.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewingResultCache
{
    // ==== Methods ===========================================================

    // ------------------------------------------------------------------------
    /**
     * Initializes the instance of the previewing result cache.
     */
    public PreviewingResultCache()
    {
        resultCache = new Hashtable<String, List<Object[]>>();
        pendingCache = new Hashtable<String, CountDownLatch>();
    }


    // ------------------------------------------------------------------------
    public void setServerCredentials(String newUrl, String newUser,
            String newPass)
    {
        if ((newUrl != null && !newUrl.equals(serverUrl))
                || (newUser != null && !newUser.equals(username))
                || (newPass != null && !newPass.equals(password)))
        {
            serverUrl = newUrl;
            username = newUser;
            password = newPass;

            reset();
        }
    }


    // ------------------------------------------------------------------------
    public void setMaxRecords(int value)
    {
        maxRecords = value;

        if (value != maxRecords)
            reset();
    }


    // ------------------------------------------------------------------------
    public void setTimeout(int value)
    {
        timeout = value;
    }


    // ------------------------------------------------------------------------
    /**
     * Clears the cache, forcing the data to be retrieved again from the Web-CAT
     * server.
     */
    public void reset()
    {
        synchronized (resultCache)
        {
            resultCache.clear();
        }
    }


    // ------------------------------------------------------------------------
    public void reset(String uuid)
    {
        synchronized (resultCache)
        {
            resultCache.remove(uuid);
        }
    }


    // ------------------------------------------------------------------------
    public void ensureResultsAreCached(String uuid, String entityName,
            String[] expressions, boolean blockIfNotCached)
    {
        boolean cached;

        synchronized (resultCache)
        {
            cached = resultCache.containsKey(uuid);
        }

        if (!cached)
        {
            synchronized (pendingCache)
            {
                boolean pending = pendingCache.containsKey(uuid);

                if (!pending)
                {
                    CountDownLatch latch = new CountDownLatch(1);
                    pendingCache.put(uuid, latch);

                    Job job = new PreviewingResultJob(
                            Messages.RESULT_CACHE_DESCRIPTION,
                            serverUrl, username, password, maxRecords, timeout,
                            uuid, entityName, expressions);
                    job.schedule();
                }
            }

            // Wait for the job to finish if we want to block.
            if (blockIfNotCached)
            {
                CountDownLatch latch;

                synchronized (pendingCache)
                {
                    latch = pendingCache.get(uuid);
                }

                if (latch != null)
                {
                    // If we found a latch in the pending cache, then the job
                    // is still running so we need to block until it completes
                    // and counts the latch down to zero.
                    try
                    {
                        latch.await();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // ------------------------------------------------------------------------
    public void movePendingToCached(String uuid, List<Object[]> results)
    {
        synchronized (pendingCache)
        {
            synchronized (resultCache)
            {
                resultCache.put(uuid, results);

                CountDownLatch latch = pendingCache.remove(uuid);

                if (latch != null)
                    latch.countDown();
            }
        }
    }


    // ------------------------------------------------------------------------
    public void cancelPending(String uuid)
    {
        synchronized (pendingCache)
        {
            CountDownLatch latch = pendingCache.remove(uuid);

            if (latch != null)
                latch.countDown();
        }
    }


    // ------------------------------------------------------------------------
    public int getRowCount(String uuid)
    {
        synchronized (resultCache)
        {
            List<Object[]> results = resultCache.get(uuid);

            if (results != null)
                return results.size();
            else
                return 0;
        }
    }


    // ------------------------------------------------------------------------
    public Object getValue(String uuid, int row, int column)
    {
        synchronized (resultCache)
        {
            List<Object[]> rows = resultCache.get(uuid);
            return rows.get(row)[column];
        }
    }


    // ==== Fields ============================================================

    private String serverUrl;

    private String username;

    private String password;

    private int maxRecords;

    private int timeout;

    /**
     * The result cache, a mapping from result set UUIDs to rows.
     */
    private Map<String, List<Object[]>> resultCache;

    private Map<String, CountDownLatch> pendingCache;
}
