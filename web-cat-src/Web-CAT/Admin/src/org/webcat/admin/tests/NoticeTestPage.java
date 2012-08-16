/*==========================================================================*\
 |  $Id: NoticeTestPage.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

package org.webcat.admin.tests;

import org.webcat.core.*;
import com.webobjects.appserver.*;

// -------------------------------------------------------------------------
/**
 *  A simple test page for exercising error/confirmation message features.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class NoticeTestPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new NoticeTestPage object.
     * @param context The context to use
     */
    public NoticeTestPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public String stateMsg;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        advanceState();
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public void advanceState()
    {
        state = (state + 1) % messages.length;
        stateMsg = messages[state];
        switch (state)
        {
            case 1:
                error("this is error message 1.");
                break;
            case 2:
                warning("this is warning message 2.");
                break;
            case 3:
                error("this is error message 3.");
                warning("this is warning message 4.");
                break;
            case 4:
                confirmationMessage("this is confirmation message 5.");
                confirmationMessage("this is confirmation message 6.");
                break;
            default:
                // Do nothing
        }
    }


    // ----------------------------------------------------------
    public WOComponent refresh()
    {
        return null;
    }


    //~ Instance/static variables .............................................

    private int state = -1;
    private static final String[] messages = {
        "Now in state 0, with no messages.",
        "Now in state 1, with one error message.",
        "Now in state 2, with one warning message.",
        "Now in state 3, with one error message and one warning message.",
        "Now in state 4, with two confirmation messages."
    };
}
