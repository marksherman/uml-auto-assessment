/*==========================================================================*\
 |  $Id: OpinionsDatabaseUpdates.java,v 1.5 2010/10/19 23:34:58 stedwar2 Exp $
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

import java.sql.SQLException;
import org.webcat.dbupdate.UpdateSet;

//-------------------------------------------------------------------------
/**
 * This class captures the SQL database schema for the database tables
 * underlying the Opinions subsystem and the Opinions.eomodeld.  Logging
 * output for this class uses its parent class' logger.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.5 $, $Date: 2010/10/19 23:34:58 $
 */
public class OpinionsDatabaseUpdates
    extends UpdateSet
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * The default constructor uses the name "grader" as the unique
     * identifier for this subsystem and EOModel.
     */
    public OpinionsDatabaseUpdates()
    {
        super("opinions");
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Creates all tables in their baseline configuration, as needed.
     * @throws SQLException on error
     */
    public void updateIncrement0() throws SQLException
    {
        createSurveyReminderJobTable();
        createSurveyResponseTable();
    }


    // ----------------------------------------------------------
    /**
     * Changes course CRNs to strings.
     * @throws SQLException on error
     */
    public void updateIncrement1() throws SQLException
    {
        database().executeSQL(
            "alter table TSurveyReminderJob add suspensionReason MEDIUMTEXT" );
    }


    // ----------------------------------------------------------
    /**
     * Add indexes for better performance.
     * @throws SQLException on error
     */
    public void updateIncrement2() throws SQLException
    {
        // Indices for SurveyReminderJob
        // None needed

        // Indices for SurveyResponse
        createIndexFor("TSurveyResponse", "assignmentOfferingId");
        createIndexFor("TSurveyResponse", "userId");
    }


    // ----------------------------------------------------------
    /**
     * Add SurveyNotificationMarker table.
     * @throws SQLException on error
     */
    public void updateIncrement3() throws SQLException
    {
        database().executeSQL(
            "alter table TSurveyReminderJob drop suspensionReason" );
        createSurveyNotificationMarkerTable();
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Create the TSurveyReminderJob table, if needed.
     * @throws SQLException on error
     */
    private void createSurveyReminderJobTable() throws SQLException
    {
        if ( !database().hasTable( "TSurveyReminderJob" ) )
        {
            log.info( "creating table TSurveyReminderJob" );
            database().executeSQL(
                "CREATE TABLE TSurveyReminderJob "
                + "(assignmentOfferingId INTEGER , "
                + "dueTime DATETIME , "
                + "OID INTEGER NOT NULL )");
            database().executeSQL(
                "ALTER TABLE TSurveyReminderJob ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TSurveyResponse table, if needed.
     * @throws SQLException on error
     */
    private void createSurveyResponseTable() throws SQLException
    {
        if ( !database().hasTable( "TSurveyResponse" ) )
        {
            log.info( "creating table TSurveyResponse" );
            database().executeSQL(
                "CREATE TABLE TSurveyResponse "
                + "(assignmentOfferingId INTEGER , "
                + "OID INTEGER NOT NULL, "
                + "isStaff BIT , "
                + "q1 TINYINT , "
                + "q2 TINYINT , "
                + "q3 TINYINT , "
                + "q4 TINYINT , "
                + "q5 TINYINT , "
                + "q6 TINYINT , "
                + "submitTime DATETIME , "
                + "text1 TEXT , "
                + "text2 TEXT , "
                + "text3 TEXT , "
                + "text4 TEXT , "
                + "userId INTEGER )");
            database().executeSQL(
                "ALTER TABLE TSurveyResponse ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the SurveyNotificationMarker table, if needed.
     * @throws SQLException on error
     */
    private void createSurveyNotificationMarkerTable() throws SQLException
    {
        if ( !database().hasTable( "SurveyNotificationMarker" ) )
        {
            log.info( "creating table SurveyNotificationMarker" );
            database().executeSQL(
                "CREATE TABLE SurveyNotificationMarker "
                + "(assignmentOfferingId INTEGER NOT NULL, "
                + "OID INTEGER NOT NULL )");
            database().executeSQL(
                "ALTER TABLE SurveyNotificationMarker ADD PRIMARY KEY (OID)" );
            createIndexFor("SurveyNotificationMarker", "assignmentOfferingId");
        }
    }
}
