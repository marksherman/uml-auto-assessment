/*==========================================================================*\
 |  $Id: GoogleVoiceDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
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

package org.webcat.notifications.googlevoice;

import java.io.IOException;

//-------------------------------------------------------------------------
/**
 * An abstract class that contains methods called by the {@link GoogleVoice}
 * class when operations succeed or fail. Users should extend this class and
 * implement whichever notification methods they are interested in receiving.
 *
 * @author  Tony Allevato
 * @version $Id: GoogleVoiceDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public abstract class GoogleVoiceDelegate
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called if log-in to Google Voice is successful.
     *
     * @param gv the GoogleVoice object
     */
    public void loginSucceeded(GoogleVoice gv)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called if log-in to Google Voice failed.
     *
     * @param gv the GoogleVoice object
     * @param e an exception describing the failure
     */
    public void loginFailed(GoogleVoice gv, IOException e)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called after an SMS message is successfully sent.
     *
     * @param gv the GoogleVoice object
     */
    public void sendSMSSucceeded(GoogleVoice gv)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called if sending an SMS message fails.
     *
     * @param gv the GoogleVoice object
     * @param e an exception describing the failure
     */
    public void sendSMSFailed(GoogleVoice gv, IOException e)
    {
        // Default implementation does nothing.
    }
}
