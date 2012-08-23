/*==========================================================================*\
 |  $Id: NotificationsDatabaseUpdates.java,v 1.2 2010/09/27 00:40:53 stedwar2 Exp $
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

package org.webcat.notifications;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.webcat.dbupdate.UpdateSet;

//-------------------------------------------------------------------------
/**
 * This class captures the SQL database schema for the database tables
 * underlying the Notifications subsystem and the Notifications.eomodeld.
 * Logging output for this class uses its parent class' logger.
 *
 * @author  Tony Allevato
 * @author  Latest changes by: $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:40:53 $
 */
public class NotificationsDatabaseUpdates
    extends UpdateSet
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * The default constructor uses the name "notifications" as the unique
     * identifier for this subsystem and EOModel.
     */
    public NotificationsDatabaseUpdates()
    {
        super("notifications");
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Creates all tables in their baseline configuration, as needed.
     * @throws SQLException on error
     */
    @Override
    public void updateIncrement0() throws SQLException
    {
        // These three have been moved here from Core. They are guarded by the
        // usual hasTable checks to ensure that they are not re-created if they
        // already exist from an older update.

        createBroadcastMessageSubscriptionTable();
        createUserMessageSubscriptionTable();
        createProtocolSettingsTable();

        createSendMessageJobTable();
        createSendMessageJobUserTable();

        database().executeSQL(
                "alter table ProtocolSettings add userId INTEGER");
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Create the BroadcastMessagingSelection table, if needed.
     * @throws SQLException on error
     */
    private void createBroadcastMessageSubscriptionTable() throws SQLException
    {
        if ( !database().hasTable( "BroadcastMessageSubscription" ) )
        {
            log.info( "creating table BroadcastMessageSubscription" );
            database().executeSQL(
                "CREATE TABLE BroadcastMessageSubscription "
                + "(OID INTEGER NOT NULL, "
                + "isEnabled BIT NOT NULL, "
                + "messageType MEDIUMTEXT , "
                + "protocolType MEDIUMTEXT )" );
            database().executeSQL(
                "ALTER TABLE BroadcastMessageSubscription ADD PRIMARY KEY (OID)"
            );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the UserMessagingSelection table, if needed.
     * @throws SQLException on error
     */
    private void createUserMessageSubscriptionTable() throws SQLException
    {
        if ( !database().hasTable( "UserMessageSubscription" ) )
        {
            log.info( "creating table UserMessageSubscription" );
            database().executeSQL(
                "CREATE TABLE UserMessageSubscription "
                + "(OID INTEGER NOT NULL, "
                + "userId INTEGER, "
                + "isEnabled BIT NOT NULL, "
                + "messageType MEDIUMTEXT , "
                + "protocolType MEDIUMTEXT )" );
            database().executeSQL(
                "ALTER TABLE UserMessageSubscription ADD PRIMARY KEY (OID)"
            );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the ProtocolSettings table, if needed.
     * @throws SQLException on error
     */
    private void createProtocolSettingsTable() throws SQLException
    {
        if ( !database().hasTable( "ProtocolSettings" ) )
        {
            log.info( "creating table ProtocolSettings" );
            database().executeSQL(
                "CREATE TABLE ProtocolSettings "
                + "(OID INTEGER NOT NULL, "
                + "settings BLOB, "
                + "parentId INTEGER, "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL )" );
            database().executeSQL(
                "ALTER TABLE ProtocolSettings ADD PRIMARY KEY (OID)"
            );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the SendMessageJob table, if needed.
     * @throws SQLException on error
     */
    private void createSendMessageJobTable() throws SQLException
    {
        if ( !database().hasTable( "SendMessageJob" ) )
        {
            log.info( "creating table SendMessageJob" );

            database().executeSQL("CREATE TABLE SendMessageJob ("
                + "OID INTEGER NOT NULL , "
                + "attachments BLOB , "
                + "broadcastProtocolSettingsId INTEGER , "
                + "broadcastProtocolSettingsSnapshot BLOB , "
                + "fullBody MEDIUMTEXT , "
                + "isSevere BIT NOT NULL , "
                + "links BLOB , "
                + "messageType TINYTEXT , "
                + "shortBody MEDIUMTEXT , "
                + "title MEDIUMTEXT , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL"
                + " )");
            database().executeSQL(
                "ALTER TABLE SendMessageJob ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the SendMessageJobUser table, if needed.
     * @throws SQLException on error
     */
    private void createSendMessageJobUserTable() throws SQLException
    {
        if ( !database().hasTable( "SendMessageJobUser" ) )
        {
            log.info( "creating table SendMessageJobUser" );

            database().executeSQL("CREATE TABLE SendMessageJobUser ("
                + "sendMessageJobId INTEGER NOT NULL , "
                + "userId INTEGER NOT NULL"
                + " )" );
            database().executeSQL(
                "ALTER TABLE SendMessageJobUser ADD PRIMARY KEY ("
                    + "sendMessageJobId, userId)" );
        }
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( UpdateSet.class );
}
