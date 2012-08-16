/*==========================================================================*\
 |  $Id: BatchResultPage.java,v 1.4 2012/01/05 20:01:44 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010-2012 Virginia Tech
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

package org.webcat.batchprocessor;

import org.webcat.core.EntityResourceRequestHandler;
import org.webcat.core.WCComponent;
import org.webcat.jobqueue.WCPageWithJobMonitoring;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
 * Displays the results of a batch job.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2012/01/05 20:01:44 $
 */
public class BatchResultPage extends WCComponent
    implements WCPageWithJobMonitoring.Delegate
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public BatchResultPage(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public BatchResult result;
    public BatchFeedbackSection feedbackSection;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public BatchResultPage self()
    {
        return this;
    }


    // ----------------------------------------------------------
    public NSArray<BatchFeedbackSection> feedbackSections()
    {
        if (result.isComplete() && cachedFeedbackSections == null)
        {
            cachedFeedbackSections =
                result.sortedVisibleFeedbackSections(user());
        }

        return cachedFeedbackSections;
    }


    // ----------------------------------------------------------
    public String feedbackSectionBaseURL()
    {
        return EntityResourceRequestHandler.urlForEntityResource(
                context(), result, null);
    }


    // ----------------------------------------------------------
    public String feedbackSectionResourceURL()
    {
        return EntityResourceRequestHandler.urlForEntityResource(
                context(), result, feedbackSection.fileName());
    }


    // ----------------------------------------------------------
    public WOActionResults jobWasCancelled()
    {
        return pageWithName(PickBatchResultToViewPage.class);
    }


    // ----------------------------------------------------------
    public boolean feedbackSectionIsOpen()
    {
        return !feedbackSection.isCollapsed();
    }


    // ----------------------------------------------------------
    public void setFeedbackSectionIsOpen(boolean value)
    {
        // ignore set calls ... just for KVC compliance.
    }


    //~ Static/instance variables .............................................

    private NSArray<BatchFeedbackSection> cachedFeedbackSections;
}
