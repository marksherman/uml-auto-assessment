/*==========================================================================*\
 |  $Id: SurveyReminderMessage.java,v 1.3 2011/12/25 21:18:24 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010-2011 Virginia Tech
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

package org.webcat.opinions.messaging;

import org.webcat.core.User;
import org.webcat.core.WCProperties;
import org.webcat.core.messaging.Message;
import org.webcat.core.messaging.SingleUserMessage;
import com.webobjects.foundation.NSDictionary;

//-------------------------------------------------------------------------
/**
 * A message that is sent to the student when an engagement/frustration
 * survey is available for an assignment.
 *
 * @author  Stephen Edwards
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.3 $ $Date: 2011/12/25 21:18:24 $
 */
public class SurveyReminderMessage
    extends SingleUserMessage
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public SurveyReminderMessage(User user, WCProperties properties)
    {
        super(user);
        this.properties = properties;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called by the subsystem init() to register the message.
     */
    public static void register()
    {
        Message.registerMessage(
            SurveyReminderMessage.class,
            "Opinions",
            "Survey Announcement",
            false,
            User.STUDENT_PRIVILEGES);
    }


    // ----------------------------------------------------------
    @Override
    public String fullBody()
    {
        return properties.stringForKeyWithDefault(
            "survey.email.body",
            "To help improve assignments in your course, you can provide "
            + "your opinions\nabout what was interesting or frustrating to "
            + "you.  This short survey should\nonly take a few minutes.  "
            + "Login to Web-CAT to complete the survey on your "
            + "assignment: \"${assignment.title}\".\n");
    }


    // ----------------------------------------------------------
    @Override
    public String shortBody()
    {
        return properties.stringForKeyWithDefault(
            "survey.email.short.body",
            "Login to Web-CAT to complete a short survey on your "
            + "assignment: \"${assignment.title}\".\n\n");
    }


    // ----------------------------------------------------------
    @Override
    public NSDictionary<String, String> links()
    {
        return new NSDictionary<String, String>(
            properties.stringForKey("survey.link"),
            "Complete your survey");
    }


    // ----------------------------------------------------------
    @Override
    public String title()
    {
        return properties.stringForKeyWithDefault(
            "survey.email.title",
            "[Opinions] Tell us about: \"${assignment.title}\"");
    }


    //~ Static/instance variables .............................................

    private WCProperties properties;
}
