/*==========================================================================*\
 |  $Id: SubmissionStats.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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

package org.webcat.webapi;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import er.extensions.foundation.ERXValueUtilities;
import org.apache.log4j.Logger;
import org.webcat.grader.Submission;

//-------------------------------------------------------------------------
/**
 * XML Response page for webapi/submissionStats requests.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class SubmissionStats
    extends XmlResponsePage
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public SubmissionStats(WOContext context)
    {
        super(context);
    }


    //~ KVC Properties ........................................................

    public Submission submission;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        int subId = ERXValueUtilities.intValue(
            context.request().formValueForKey("submission"));
        if (subId == 0)
        {
            log.warn("no submission id parameter specified");
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }
        submission = Submission.forId(session().sessionContext(), subId);
        if (submission == null)
        {
            log.warn("no submission found");
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }
        if (!submission.assignmentOffering().courseOffering().instructors()
                .contains(session().user())
            && !submission.assignmentOffering().courseOffering().graders()
                .contains(session().user()))
        {
            log.error("unauthorized attempt to retrieve submission data "
                + "for " + submission
                + " in course "
                + submission.assignmentOffering().courseOffering()
                + " by " + session().user());
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }

        // Finally, generate the response
        super.appendToResponse(response, context);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(SubmissionStats.class);
}
