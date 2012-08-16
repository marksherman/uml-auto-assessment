/*==========================================================================*\
 |  $Id: SurveyReminderWorker.java,v 1.3 2011/01/21 18:11:59 stedwar2 Exp $
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

package org.webcat.opinions;

import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.User;
import org.webcat.core.WCProperties;
import org.webcat.grader.AssignmentOffering;
import org.webcat.jobqueue.WorkerThread;
import org.webcat.opinions.messaging.SurveyReminderMessage;
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
 * A {@link WorkerThread} subclass for processing survey reminders.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/01/21 18:11:59 $
 */
public class SurveyReminderWorker
    extends WorkerThread<SurveyReminderJob>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param descriptor the descriptor for this worker thread
     */
    public SurveyReminderWorker()
    {
        super(SurveyReminderJob.ENTITY_NAME);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Processes one survey reminder job by sending out e-mail to everyone
     * who submitted to the given assignment offering, as well as all
     * instructors assigned to teach the corresponding course offering.
     */
    protected void processJob()
    {
        AssignmentOffering assignment = currentJob().assignmentOffering();
        if (assignment == null)
        {
            log.error("Null assignment offering on job!");
            return;
        }
        System.out.println("Processing SurveyReminderJob for " + assignment);

        // for all students (not staff) in course offering
        for (User user : assignment.courseOffering().studentsWithoutStaff())
        {
            // check to see if student has completed a survey
            NSArray<SurveyResponse> responses = SurveyResponse
                .responsesForAssignmentOfferingAndUser(
                    localContext(), assignment, user);

            // if not, call method below
            if (responses == null || responses.count() == 0)
            {
                notifyStudent(user, assignment);
            }
        }

        // Record that notifications have been sent
// FIXME:        SurveyNotificationMarker.create(localContext(), assignment);
        localContext().saveChanges();
    }


    // ----------------------------------------------------------
    private void notifyStudent(User user, AssignmentOffering assignment)
    {
        WCProperties properties =
            new WCProperties(Application.configurationProperties());
        user.addPropertiesTo(properties);
        if (assignment != null)
        {
            properties.setProperty("assignment.title",
                assignment.titleString());
        }
        properties.setProperty("survey.link",
            Application.configurationProperties().getProperty("base.url")
            + "?page=opinions");

        try
        {
            log.debug("sending survey reminder message to "
                + user + " for " + assignment);
// FIXME:           new SurveyReminderMessage(user, properties).send();
        }
        catch (Exception e)
        {
            log.error("Unable to notify student " + user
                + " of survey availability for " + assignment, e);
        }
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(SurveyReminderWorker.class);
}
