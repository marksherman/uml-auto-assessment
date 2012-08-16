/*==========================================================================*\
 |  $Id: ReportCompleteMessage.java,v 1.2 2011/12/06 18:42:09 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

package org.webcat.reporter.messaging;

import org.webcat.core.User;
import org.webcat.core.messaging.Message;
import org.webcat.reporter.GeneratedReport;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
 * A message that is sent to the user who requested that a report be generated,
 * once the report is complete.
 *
 * @author Tony Allevato
 * @version $Id: ReportCompleteMessage.java,v 1.2 2011/12/06 18:42:09 stedwar2 Exp $
 */
public class ReportCompleteMessage extends Message
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the ReportCompleteMessage class.
     *
     * @param report the report that was completed
     */
    public ReportCompleteMessage(GeneratedReport report)
    {
        EOEditingContext ec = editingContext();
        try
        {
            ec.lock();
            this.report = report.localInstance(ec);
        }
        finally
        {
            ec.unlock();
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called by the subsystem init() to register the message.
     */
    public static void register()
    {
        Message.registerMessage(
                ReportCompleteMessage.class,
                "Reports",
                "Report Completed",
                false,
                User.GRADER_PRIVILEGES);
    }


    // ----------------------------------------------------------
    @Override
    public String fullBody()
    {
        // TODO make this better
        return "The report named \"" + report.description() +
            "\" was generated.";
    }


    // ----------------------------------------------------------
    @Override
    public String shortBody()
    {
        return "The report named \"" + report.description() +
            "\" was generated.";
    }


    // ----------------------------------------------------------
    @Override
    public String title()
    {
        return "Report completed: " + report.description();
    }


    // ----------------------------------------------------------
    @Override
    public NSArray<User> users()
    {
        // Returns an array containing the one user who generated this report.
        EOEditingContext ec = editingContext();
        try
        {
            ec.lock();
            return new NSArray<User>(report.user());
        }
        finally
        {
            ec.unlock();
        }
    }


    //~ Static/instance variables .............................................

    private GeneratedReport report;
}
