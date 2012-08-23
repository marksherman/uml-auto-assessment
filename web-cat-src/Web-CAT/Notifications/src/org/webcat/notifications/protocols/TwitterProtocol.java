/*==========================================================================*\
 |  $Id: TwitterProtocol.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
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

import org.apache.log4j.Logger;
import org.webcat.core.User;
import org.webcat.core.messaging.IMessageSettings;
import org.webcat.notifications.SendMessageJob;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

//-------------------------------------------------------------------------
/**
 * A notification protocol that delivers messages as Twitter feed updates.
 *
 * @author Tony Allevato
 * @version $Id: TwitterProtocol.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public class TwitterProtocol extends Protocol
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public TwitterProtocol()
    {
        twitterFactory = new AsyncTwitterFactory(new Adapter());
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    public void sendMessage(SendMessageJob message,
                            User user,
                            IMessageSettings settings)
    throws Exception
    {
        final String username =
            settings.stringSettingForKey(USERNAME_SETTING, null);
        String password = settings.stringSettingForKey(PASSWORD_SETTING, null);

        if (username == null || password == null)
        {
            return;
        }

        String content = message.shortBody();

        if (content.length() > 140)
        {
            content = content.substring(0, 137) + "...";
        }

        AsyncTwitter twitter = twitterFactory.getInstance(username, password);
        twitter.updateStatus(content);
    }


    // ----------------------------------------------------------
    @Override
    public boolean isBroadcast()
    {
        return true;
    }


    // ----------------------------------------------------------
    @Override
    public String name()
    {
        return "Twitter";
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    private class Adapter extends TwitterAdapter
    {
        // ----------------------------------------------------------
        @Override
        public void onException(TwitterException e, TwitterMethod method)
        {
            if (method == TwitterMethod.UPDATE_STATUS)
            {
                log.warn("An error occurred when updating the Twitter "
                        + "feed", e);
            }
        }
    }

    //~ Static/instance variables .............................................

    private AsyncTwitterFactory twitterFactory;

    private static final String USERNAME_SETTING =
        "org.webcat.notifications.protocols.TwitterProtocol.username";
    private static final String PASSWORD_SETTING =
        "org.webcat.notifications.protocols.TwitterProtocol.password";

    private static final Logger log = Logger.getLogger(TwitterProtocol.class);
}
