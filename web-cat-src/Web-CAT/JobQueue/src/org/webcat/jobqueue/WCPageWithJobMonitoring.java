/*==========================================================================*\
 |  $Id: WCPageWithJobMonitoring.java,v 1.2 2010/09/27 00:30:22 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009-2010 Virginia Tech
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

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;
import org.webcat.core.WCComponent;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

//-------------------------------------------------------------------------
/**
 * Base page that includes job monitoring support.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:30:22 $
 */
public class WCPageWithJobMonitoring
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public WCPageWithJobMonitoring(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public String title;
    public String extraRequires;
    public String permalink;

    public JobBase job;
    public Delegate delegate;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public boolean isJobValid()
    {
        return job != null && !job.isDeletedEO();
    }


    // ----------------------------------------------------------
    public WOActionResults reloadPage()
    {
        return null;
    }


    // ----------------------------------------------------------
    public synchronized JSONObject pollJobStatus()
    {
        JSONObject result = new JSONObject();

        try
        {
            if (!isJobValid())
            {
                result.put("isComplete", true);
                job = null;
            }
            else
            {
                result.put("isComplete", false);

                if (job.worker() == null)
                {
                    result.put("isStarted", false);
                    // FIXME get actual queue position
                    result.put("queuePosition", 1);

                    if (previousReadyState == null
                            || job.isReady() != previousReadyState)
                    {
                        result.put("readyStateDidChange", true);
                    }

                    previousReadyState = job.isReady();
                }
                else
                {
                    result.put("isStarted", true);
                }

                result.put("progress", job.progressPercentage());
                result.put("progressMessage", job.progressMessage());
            }
        }
        catch (JSONException e)
        {
            // Do nothing.
        }

        return result;
    }


    // ----------------------------------------------------------
    public WOActionResults cancelJob()
    {
        if (job != null)
        {
            job.setIsCancelled(true);
            job.editingContext().saveChanges();

            return delegate.jobWasCancelled();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public WOActionResults deleteJob()
    {
        if (job != null)
        {
            job.delete();
            job.editingContext().saveChanges();

            return delegate.jobWasCancelled();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public WOActionResults restartJob()
    {
        if (job != null)
        {
            job.setIsReady(true);
            job.editingContext().saveChanges();
        }

        return null;
    }


    //~ Nested interfaces .....................................................

    // ----------------------------------------------------------
    /**
     * Methods used by the job wrapper to communicate with the component that
     * owns it.
     */
    public static interface Delegate
    {
        // ----------------------------------------------------------
        /**
         * Called when the user clicks the "Cancel Job" button, after the job
         * is marked as cancelled. This method should return a new page that
         * the user will be directed to.
         *
         * @return the page to direct the user to
         */
        WOActionResults jobWasCancelled();
    }


    //~ Static/instance variables .............................................

    private Boolean previousReadyState;
}
