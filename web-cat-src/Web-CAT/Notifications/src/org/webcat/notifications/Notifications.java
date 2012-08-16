/*==========================================================================*\
 |  $Id: Notifications.java,v 1.4 2011/12/25 21:18:26 stedwar2 Exp $
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

package org.webcat.notifications;

import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.SentMessage;
import org.webcat.core.Subsystem;
import org.webcat.jobqueue.QueueDescriptor;
import org.webcat.woextensions.ECAction;
import static org.webcat.woextensions.ECAction.run;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

//-------------------------------------------------------------------------
/**
 * The primary class of the Notifications subsystem.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2011/12/25 21:18:26 $
 */
public class Notifications
    extends Subsystem
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new BatchProcessor subsystem object.
     */
    public Notifications()
    {
        super();

        instance = this;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void init()
    {
        super.init();

        // Initialize the system-wide broadcast settings object if it does not
        // already exist (this should only occur when the application is
        // started for the first time.

        run(new ECAction() { public void action() {
            if (ProtocolSettings.systemSettings(ec) == null)
            {
                ProtocolSettings settings = new ProtocolSettings();
                EOGlobalID gid = EOKeyGlobalID.globalIDWithEntityName(
                        ProtocolSettings.ENTITY_NAME, new Long[] {
                                Long.valueOf(1) });

                ec.insertObjectWithGlobalID(settings, gid);
                ec.saveChanges();
            }

            // 30 days ago
            NSTimestamp limit = new NSTimestamp()
                .timestampByAddingGregorianUnits(0, 0, -30, 0, 0, 0);

            NSArray<SentMessage> messages =
                SentMessage.objectsMatchingQualifier(
                    ec,
                    SentMessage.sentTime.before(limit));
            log.info("deleting " + messages.size()
                + " SentMessage objects over 30 days old");
            for (SentMessage message : messages)
            {
                message.delete();
            }
            ec.saveChanges();
        }});

        // Register the batch job queue and create worker threads.

        QueueDescriptor.registerQueue(SendMessageJob.ENTITY_NAME);
    }


    // ----------------------------------------------------------
    public void start()
    {
        // Register the message dispatcher with the application, overriding
        // the built-in fallback dispatcher.

        Application.wcApplication().setMessageDispatcher(
            MessageDispatcher.sharedDispatcher());

        log.info("Starting SendMessageJob worker thread");

        new SendMessageWorkerThread().start();
    }


    // ----------------------------------------------------------
    /**
     * Returns the sole instance of the notifications subsystem.
     *
     * @return the Notifications object that represents the subsystem.
     */
    public static Notifications getInstance()
    {
        return instance;
    }


    //~ Instance/static variables .............................................

    /**
     * This is the sole instance of the notifications subsystem, initialized
     * by the constructor.
     */
    private static Notifications instance;

    private static Logger log = Logger.getLogger(Notifications.class);
}
