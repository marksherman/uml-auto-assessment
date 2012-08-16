/*==========================================================================*\
 |  $Id: SMSProtocol.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
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

package org.webcat.notifications.protocols;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.webcat.core.User;
import org.webcat.core.messaging.IMessageSettings;
import org.webcat.notifications.ProtocolSettings;
import org.webcat.notifications.SendMessageJob;
import org.webcat.notifications.googlevoice.GoogleVoice;
import org.webcat.notifications.googlevoice.GoogleVoiceDelegate;

//-------------------------------------------------------------------------
/**
 * A notification protocol that delivers messages as mobile SMS messages, using
 * Google Voice.
 *
 * @author Tony Allevato
 * @version $Id: SMSProtocol.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public class SMSProtocol extends Protocol
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new SMS protocol.
     */
    public SMSProtocol()
    {
        loginLatch = new CountDownLatch(1);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    public void sendMessage(SendMessageJob message,
                            User user,
                            IMessageSettings settings)
    throws Exception
    {
        if (googleVoice == null)
        {
            ProtocolSettings systemSettings = ProtocolSettings.systemSettings(
                    message.editingContext());

            String username =
                systemSettings.stringSettingForKey(ACCOUNT_EMAIL_SETTING, null);
            String password =
                systemSettings.stringSettingForKey(PASSWORD_SETTING, null);

            if (username != null && password != null)
            {
                log.info("sendMessage: Logging into Google Voice with "
                        + "username " + username);

                googleVoice = new GoogleVoice(username, password,
                        "webcat", new Delegate());
                googleVoice.login();
            }
            else
            {
                log.warn("sendMessage: Either the Google Voice username "
                        + "or password was not provided; use the Messaging "
                        + "page in the Admin section to configure this");
                return;
            }
        }

        log.info("sendMessage: Waiting for Google Voice log-in");
        loginLatch.await(10, TimeUnit.SECONDS);

        if (loginWasSuccessful)
        {
            String mobileNumber = settings.stringSettingForKey(
                    MOBILE_NUMBER_SETTING, null);

            String content = message.shortBody();

            if (mobileNumber != null)
            {
                googleVoice.sendSMS(mobileNumber, content);
            }
        }
        else
        {
            googleVoice = null;

            log.info("sendMessage: 10 seconds passed without a response "
                    + "from Google Voice");
        }
    }


    // ----------------------------------------------------------
    @Override
    public boolean isBroadcast()
    {
        return false;
    }


    // ----------------------------------------------------------
    @Override
    public String name()
    {
        return "Mobile Message";
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    private class Delegate extends GoogleVoiceDelegate
    {
        // ----------------------------------------------------------
        public void loginSucceeded(GoogleVoice gv)
        {
            log.info("Successfully logged in to Google Voice account");
            loginLatch.countDown();
            loginWasSuccessful = true;
        }


        // ----------------------------------------------------------
        public void loginFailed(GoogleVoice gv, IOException e)
        {
            log.error("Failed to log in to Google Voice account", e);

            loginLatch.countDown();
            loginWasSuccessful = false;
        }


        // ----------------------------------------------------------
        public void sendSMSFailed(GoogleVoice gv, IOException e)
        {
            log.error("Failed to send SMS message", e);
        }
    }


    //~ Static/instance variables .............................................

    private static final String ACCOUNT_EMAIL_SETTING =
        "org.webcat.notifications.protocols.SMSProtocol.accountEmail";
    private static final String PASSWORD_SETTING =
        "org.webcat.notifications.protocols.SMSProtocol.password";

    private static final String MOBILE_NUMBER_SETTING =
        "org.webcat.notifications.protocols.SMSProtocol.mobileNumber";

    private CountDownLatch loginLatch;
    private boolean loginWasSuccessful;
    private GoogleVoice googleVoice;

    private static final Logger log = Logger.getLogger(SMSProtocol.class);
}
