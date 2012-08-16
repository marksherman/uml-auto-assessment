/*==========================================================================*\
 |  $Id: OpinionsHomeStatus.java,v 1.2 2011/01/21 18:11:59 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010 Virginia Tech
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

package org.webcat.opinions;

import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.CourseOffering;
import org.webcat.core.WCComponent;
import org.webcat.grader.Assignment;
import org.webcat.grader.AssignmentOffering;
import org.webcat.grader.Submission;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXS;

//-------------------------------------------------------------------------
/**
 *  Generates the opinions subsystem's page sections for the home->status
 *  page.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/01/21 18:11:59 $
 */
public class OpinionsHomeStatus
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The page's context
     */
    public OpinionsHomeStatus(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public AssignmentOffering assignmentOffering;
    public ERXDisplayGroup<Assignment> assignmentOfferingDisplayGroup;
    public int index;
    public boolean hasSurveys;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        log.debug("starting appendToResponse()");
        if (pendingOpinions == null)
        {
            if (log.isDebugEnabled())
            {
                Application.enableSQLLogging();
            }

            // First, get all assignments that this user has submitted to
            // that accept opinion surveys
            pendingOpinions = AssignmentOffering.objectsMatchingQualifier(
                localContext(),
                AssignmentOffering.assignment
                    .dot(Assignment.trackOpinions).isTrue().and(
                    AssignmentOffering.courseOffering
                        .dot(CourseOffering.students).is(user())))
                    .mutableClone();
            if (log.isDebugEnabled())
            {
                log.debug("assignments pending feedback: " + pendingOpinions);
            }

            NSTimestamp now = new NSTimestamp();

            for (int i = 0; i < pendingOpinions.count(); i++)
            {
                AssignmentOffering thisOffering = pendingOpinions.get(i);

                // Remove any assignment offerings for which this user is
                // staff, or for which the late submission deadline has not
                // passed.
                if (thisOffering.courseOffering().isStaff(user()) ||
                    now.before(thisOffering.lateDeadline()))
                {
                    if (log.isDebugEnabled())
                    {
                        if (now.before(thisOffering.lateDeadline()))
                        {
                            log.debug("late deadline not yet passed for: "
                                + thisOffering.courseOffering() + " ("
                                + thisOffering.lateDeadline() + ")");
                        }
                        else
                        {
                            log.debug("user is staff for: "
                                + thisOffering.courseOffering());
                        }
                    }
                    pendingOpinions.remove(i);
                    --i;
                }
                else
                {
                    // Check to see if this user has already completed a
                    // survey for this one
                    NSArray<SurveyResponse> responses = SurveyResponse
                        .responsesForAssignmentOfferingAndUser(
                            localContext(), thisOffering, user());
                    if (responses.count() > 0)
                    {
                        pendingOpinions.remove(i);
                        --i;
                    }
                }
            }

            if (log.isDebugEnabled())
            {
                log.debug("final list: " + pendingOpinions);
                Application.disableSQLLogging();
            }
            assignmentOfferingDisplayGroup.setObjectArray(pendingOpinions);
            hasSurveys = pendingOpinions.count() > 0;
        }
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public Submission highestSubmission()
    {
        if (highest == null
            || highest.assignmentOffering() != assignmentOffering)
        {
            EOFetchSpecification fspec = new EOFetchSpecification(
                Submission.ENTITY_NAME,
                ERXQ.and(
                    Submission.user.is(user()),
                    Submission.assignmentOffering.is(assignmentOffering)),
                ERXS.descs(Submission.SUBMIT_NUMBER_KEY));
            fspec.setUsesDistinct(true);
            fspec.setFetchLimit(1);
            NSArray<Submission> result = Submission
                .objectsWithFetchSpecification(localContext(), fspec);
            if (result != null && result.count() > 0)
            {
                highest = result.objectAtIndex(0);
            }
        }
        return highest;
    }


    // ----------------------------------------------------------
    public WOComponent surveyForAssignment()
    {
        OpinionsSurveyPage page = pageWithName(OpinionsSurveyPage.class);
        page.nextPage = (WCComponent)context().page();
        page.assignmentOffering = assignmentOffering;
        pendingOpinions = null;
        return page;
    }


    //~ Instance/static variables .............................................

    private NSMutableArray<AssignmentOffering> pendingOpinions;
    private Submission highest;
    static Logger log = Logger.getLogger(OpinionsHomeStatus.class);
}
