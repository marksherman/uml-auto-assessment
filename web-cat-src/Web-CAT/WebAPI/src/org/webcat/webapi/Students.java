/*==========================================================================*\
 |  $Id: Students.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXValueUtilities;
import org.apache.log4j.Logger;
import org.webcat.core.Status;
import org.webcat.core.User;
import org.webcat.grader.AssignmentOffering;
import org.webcat.grader.Submission;

//-------------------------------------------------------------------------
/**
 * XML Response page for webapi/students requests.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class Students
    extends XmlResponsePage
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public Students(WOContext context)
    {
        super(context);
    }


    //~ KVC Properties ........................................................

    public NSMutableArray<Submission> submissions;
    public Submission                 aSubmission;
    public AssignmentOffering         assignmentOffering;

    public double  highScore           = 0.0;
    public double  lowScore            = 0.0;
    public double  avgScore            = 0.0;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        int assignmentId = ERXValueUtilities.intValue(
            context.request().formValueForKey("assignment"));
        if (assignmentId == 0)
        {
            log.warn("no assignment id parameter specified");
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }

        assignmentOffering = AssignmentOffering.forId(
            session().sessionContext(), assignmentId);
        if (assignmentOffering == null)
        {
            log.warn("no assignment found");
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }
        if (!assignmentOffering.courseOffering().instructors().contains(
                session().user())
            && !assignmentOffering.courseOffering().graders().contains(
                session().user()))
        {
            log.error("unauthorized attempt to retrieve assignment data "
                + "for " + assignmentOffering
                + " in course " + assignmentOffering.courseOffering()
                + " by " + session().user());
            SimpleMessageResponse err =
                pageWithName(SimpleMessageResponse.class);
            err.appendToResponse(response, context);
            return;
        }

        // Find the list of users to look at
        NSMutableArray<User> students =
            assignmentOffering.courseOffering().students().mutableClone();
        if (ERXValueUtilities.booleanValue(
            context.request().formValueForKey("includeStaff")))
        {
            ERXArrayUtilities.addObjectsFromArrayWithoutDuplicates(
                students,
                assignmentOffering.courseOffering().instructors());
            ERXArrayUtilities.addObjectsFromArrayWithoutDuplicates(
                students,
                assignmentOffering.courseOffering().graders());
        }

        submissions = new NSMutableArray<Submission>();
        highScore = 0.0;
        lowScore  = 0.0;
        avgScore  = 0.0;
        if ( students != null )
        {
            for (User student : students)
            {
                log.debug("checking " + student.userName());

                Submission thisSubmission = null;
                Submission mostRecentSubmission = null;
                Submission gradedSubmission = null;
                // Find the submission
                NSArray<Submission> thisSubmissionSet =
                    Submission.objectsMatchingQualifier(
                        session().sessionContext(),
                        Submission.user.eq(student).and(
                        Submission.assignmentOffering.eq(assignmentOffering)));
                log.debug("searching for submissions");
                for (Submission sub : thisSubmissionSet)
                {
                    if (mostRecentSubmission == null
                        || sub.submitNumber() >
                           mostRecentSubmission.submitNumber())
                    {
                        mostRecentSubmission = sub;
                    }
                    log.debug("\tsub #" + sub.submitNumber());
                    if (sub.result() != null && !sub.partnerLink())
                    {
                        if (thisSubmission == null)
                        {
                            thisSubmission = sub;
                        }
                        else if (sub.submitNumberRaw() != null)
                        {
                            int num = sub.submitNumber();
                            if (num > thisSubmission.submitNumber())
                            {
                                thisSubmission = sub;
                            }
                        }
                        if (sub.result().status() != Status.TO_DO)
                        {
                            if (gradedSubmission == null)
                            {
                                gradedSubmission = sub;
                            }
                            else if (sub.submitNumberRaw() != null)
                            {
                                int num = sub.submitNumber();
                                if (num > gradedSubmission.submitNumber())
                                {
                                    gradedSubmission = sub;
                                }
                            }
                        }
                    }
                }
                if (gradedSubmission != null)
                {
                    thisSubmission = gradedSubmission;
                }
                if (thisSubmission != null)
                {
                    log.debug(
                        "submission found = " + thisSubmission.submitNumber());
                    double score = thisSubmission.result().finalScore();
                    if (submissions.count() == 0)
                    {
                        highScore = score;
                        lowScore  = score;
                    }
                    else
                    {
                        if (score > highScore)
                        {
                            highScore = score;
                        }
                        if (score < lowScore)
                        {
                            lowScore = score;
                        }
                    }
                    avgScore += score;
                    submissions.addObject(thisSubmission);
                }
                else
                {
                    log.debug("no submission found");
                }
            }
        }
        if (submissions.count() > 0)
        {
            avgScore /= submissions.count();
        }

        // Finally, generate the response
        super.appendToResponse(response, context);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(Students.class);
}
