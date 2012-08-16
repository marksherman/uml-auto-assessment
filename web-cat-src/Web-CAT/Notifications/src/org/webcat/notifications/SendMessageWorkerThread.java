/*==========================================================================*\
 |  $Id: SendMessageWorkerThread.java,v 1.5 2011/12/25 21:18:26 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
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

package org.webcat.notifications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.MutableDictionary;
import org.webcat.core.User;
import org.webcat.core.messaging.IMessageSettings;
import org.webcat.core.messaging.Message;
import org.webcat.core.messaging.MessageDescriptor;
import org.webcat.jobqueue.WorkerThread;
import org.webcat.notifications.protocols.Protocol;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

//-------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.5 $, $Date: 2011/12/25 21:18:26 $
 */
public class SendMessageWorkerThread extends WorkerThread<SendMessageJob>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the SendMessageWorkerThread class.
     *
     * @param queueEntity the queue entity
     */
    public SendMessageWorkerThread()
    {
        super(SendMessageJob.ENTITY_NAME);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Implements the logic of the job.
     */
    @Override
    protected void processJob() throws IOException
    {
        sendInternal();
    }


    // ----------------------------------------------------------
    /**
     * Sends the message, after the editing context has been locked.
     *
     * @param ec the editing context to use for fetching protocol settings
     */
    private void sendInternal()
    {
        SendMessageJob job = currentJob();
        if (log.isDebugEnabled())
        {
            log.debug("Starting job: " + job.id() + ": "+ job);
        }

        MessageDescriptor descriptor =
            Message.messageDescriptorForMessageType(job.messageType());

        // Broadcast the message to any system-wide messaging protocols that
        // apply.

        if (descriptor.isBroadcast())
        {
            IMessageSettings settings;

            if (job.broadcastProtocolSettings() != null)
            {
                settings = job.broadcastProtocolSettings();
            }
            else if (job.broadcastProtocolSettingsSnapshot() != null)
            {
                settings = new MessageSettingsSnapshotWrapper(
                        job.broadcastProtocolSettingsSnapshot());
            }
            else
            {
                settings = ProtocolSettings.systemSettings(localContext());
            }

            Set<Protocol> protocols = broadcastProtocolsToSend();

            for (Protocol protocol : protocols)
            {
                try
                {
                    protocol.sendMessage(job, null, settings);
                }
                catch (Exception e)
                {
                    log.error("The following error occurred when sending " +
                            "the broadcast message \"" + job.title() + "\"", e);
                }
            }
        }

        // Send the message directly to any users to whom the message applies,
        // if they have notifications for a particular protocol enabled.

        NSArray<User> users = job.destinationUsers();
        if (users != null)
        {
            for (User user : users)
            {
                // Sanity check to ensure that messages don't get sent to users
                // who shouldn't receive them based on their access level.

                if (user.accessLevel() >= descriptor.accessLevel())
                {
                    ProtocolSettings protocolSettings =
                        ProtocolSettings.protocolSettingsForUser(user);
                    Set<Protocol> protocols = userProtocolsToSend(user);

                    for (Protocol protocol : protocols)
                    {
                        try
                        {
                            protocol.sendMessage(job, user, protocolSettings);
                        }
                        catch (Exception e)
                        {
                            log.error("The following error occurred when "
                                    + "sending the direct message \""
                                    + job.title()
                                    + "\" to user " + user.name(), e);
                        }
                    }
                }
            }
        }

        // Mail the message to system administrators if necessary.

        if (job.isSevere())
        {
            sendMailToSysAdmins();
        }
        if (log.isDebugEnabled())
        {
            log.debug("Finished job: " + job.id() + ": "+ job);
        }
    }


    // ----------------------------------------------------------
    /**
     * Sends this message to the system notification e-mail addresses that are
     * specified in the installation wizard.
     */
    private void sendMailToSysAdmins()
    {
        SendMessageJob job = currentJob();

        StringBuffer body = new StringBuffer();
        body.append(job.fullBody());
        body.append("\n\n");

        @SuppressWarnings("unchecked")
        NSDictionary<String, String> links = job.links();
        if (links != null && links.count() > 0)
        {
            body.append("Links:\n");

            for (String key : links.allKeys())
            {
                String url = links.objectForKey(key);

                body.append(key);
                body.append(": ");
                body.append(url);
                body.append("\n");
            }
        }

        Application.sendAdminEmail(
            job.title(), body.toString(), job.attachmentsAsList());
    }


    // ----------------------------------------------------------
    /**
     * Gets the set of protocols that the system administrator has enabled for
     * this type of message.
     *
     * @return the set of Protocols that the system administrator has enabled
     *     for broadcast messages
     */
    private Set<Protocol> broadcastProtocolsToSend()
    {
        SendMessageJob job = currentJob();

        String messageType = job.messageType();

        Set<Protocol> protocolsToSend = new HashSet<Protocol>();

        NSArray<Protocol> protocols =
            MessageDispatcher.sharedDispatcher().registeredProtocols(true);

        for (Protocol protocol : protocols)
        {
            if (protocol.isEnabledByDefault())
            {
                protocolsToSend.add(protocol);
            }
        }

        NSArray<BroadcastMessageSubscription> subscriptions =
            BroadcastMessageSubscription.subscriptionsForMessageType(
                    localContext(), messageType);

        for (BroadcastMessageSubscription subscription : subscriptions)
        {
            String type = subscription.protocolType();
            Protocol protocol =
                MessageDispatcher.sharedDispatcher().protocolWithName(
                    type, true);


            if (protocol != null)
            {
                if (subscription.isEnabled())
                {
                    protocolsToSend.add(protocol);
                }
                else
                {
                    protocolsToSend.remove(protocol);
                }
            }
            else
            {
                log.warn("Attempted to send broadcast message via " +
                        "unregistered protocol: " + type);
            }
        }

        return protocolsToSend;
    }


    // ----------------------------------------------------------
    /**
     * Gets the set of protocols that the specified user has enabled for a
     * particular message type.
     *
     * @param user the User who will be receiving a message
     * @param ec the editing context
     * @return the set of Protocols that this user has enabled
     */
    private Set<Protocol> userProtocolsToSend(User user)
    {
        SendMessageJob job = currentJob();

        String messageType = job.messageType();
        EOEditingContext ec = user.editingContext();

        Set<Protocol> protocolsToSend = new HashSet<Protocol>();

        NSArray<Protocol> protocols =
            MessageDispatcher.sharedDispatcher().registeredProtocols(false);

        for (Protocol protocol : protocols)
        {
            if (protocol.isEnabledByDefault())
            {
                protocolsToSend.add(protocol);
            }
        }

        NSArray<UserMessageSubscription> subscriptions =
            UserMessageSubscription.subscriptionsForMessageTypeAndUser(
                    ec, messageType, user);

        for (UserMessageSubscription subscription : subscriptions)
        {
            String type = subscription.protocolType();
            Protocol protocol =
                MessageDispatcher.sharedDispatcher().protocolWithName(
                        type, false);

            if (protocol != null)
            {
                if (subscription.isEnabled())
                {
                    protocolsToSend.add(protocol);
                }
                else
                {
                    protocolsToSend.remove(protocol);
                }
            }
            else
            {
                log.warn("Attempted to send direct message via " +
                        "unregistered protocol: " + type);
            }
        }

        return protocolsToSend;
    }


    // ----------------------------------------------------------
/*    @Override
    protected void resetJob()
    {
    }


    // ----------------------------------------------------------
    @Override
    protected String additionalSuspensionInfo()
    {
        return null;
    }*/


    // ----------------------------------------------------------
    private class MessageSettingsSnapshotWrapper implements IMessageSettings
    {
        // ----------------------------------------------------------
        public MessageSettingsSnapshotWrapper(MutableDictionary snapshot)
        {
            this.snapshot = snapshot;
        }


        // ----------------------------------------------------------
        public MutableDictionary settingsSnapshot()
        {
            return snapshot;
        }


        // ----------------------------------------------------------
        public Object settingForKey(String key)
        {
            return snapshot.objectForKey(key);
        }


        // ----------------------------------------------------------
        public String stringSettingForKey(String key, String defaultValue)
        {
            String value = (String) settingForKey(key);
            return value == null ? defaultValue : value;
        }


        // ----------------------------------------------------------
        public boolean booleanSettingForKey(String key, boolean defaultValue)
        {
            Boolean value = (Boolean) settingForKey(key);
            return value == null ? defaultValue : value;
        }


        // ----------------------------------------------------------
        public int intSettingForKey(String key, int defaultValue)
        {
            Number value = (Number) settingForKey(key);
            return value == null ? defaultValue : value.intValue();
        }


        // ----------------------------------------------------------
        public double doubleSettingForKey(String key, double defaultValue)
        {
            Number value = (Number) settingForKey(key);
            return value == null ? defaultValue : value.doubleValue();
        }


        private MutableDictionary snapshot;
    }


    //~ Static/instance variables .............................................

    private static final Logger log = Logger.getLogger(
            SendMessageWorkerThread.class);
}
