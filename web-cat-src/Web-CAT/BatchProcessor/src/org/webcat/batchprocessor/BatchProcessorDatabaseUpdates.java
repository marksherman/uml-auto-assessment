/*==========================================================================*\
 |  $Id: BatchProcessorDatabaseUpdates.java,v 1.3 2012/02/05 21:59:53 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010-2012 Virginia Tech
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

package org.webcat.batchprocessor;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.webcat.dbupdate.UpdateSet;

//-------------------------------------------------------------------------
/**
 * This class captures the SQL database schema for the database tables
 * underlying the BatchProcessor subsystem and the BatchProcessor.eomodeld.
 * Logging output for this class uses its parent class' logger.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/02/05 21:59:53 $
 */
public class BatchProcessorDatabaseUpdates
    extends UpdateSet
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * The default constructor uses the name "batchprocessor" as the unique
     * identifier for this subsystem and EOModel.
     */
    public BatchProcessorDatabaseUpdates()
    {
        super("batchprocessor");
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
        createBatchFeedbackSectionTable();
        createBatchJobTable();
        createBatchPluginTable();
        createBatchResultTable();
        createBatchResultPropertyTable();
    }


    // ----------------------------------------------------------
    /**
     * Creates all tables in their baseline configuration, as needed.
     * @throws SQLException on error
     */
    public void updateIncrement1() throws SQLException
    {
        database().executeSQL("alter table BatchJob change "
            + "batchedObjectIds batchedObjectIds LONGBLOB");
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Create the BatchJob table, if needed.
     * @throws SQLException on error
     */
    private void createBatchFeedbackSectionTable() throws SQLException
    {
        if (!database().hasTable("BatchFeedbackSection"))
        {
            log.info("creating table BatchFeedbackSection");

            database().executeSQL("CREATE TABLE BatchFeedbackSection ("
                + "OID INTEGER NOT NULL , "
                + "batchResultId INTEGER , "
                + "fileName TINYTEXT , "
                + "isCollapsed BIT NOT NULL , "
                + "isLinked BIT NOT NULL , "
                + "location TINYTEXT , "
                + "mimeType TINYTEXT , "
                + "sortOrder INTEGER , "
                + "recipients TINYTEXT , "
                + "title TINYTEXT"
                + " )");
            database().executeSQL(
                "ALTER TABLE BatchFeedbackSection ADD PRIMARY KEY (OID)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the BatchJob table, if needed.
     * @throws SQLException on error
     */
    private void createBatchJobTable() throws SQLException
    {
        if (!database().hasTable("BatchJob"))
        {
            log.info("creating table BatchJob");

            database().executeSQL("CREATE TABLE BatchJob ("
                + "OID INTEGER NOT NULL , "
                + "batchedObjectIds BLOB , "
                + "batchPluginId INTEGER , "
                + "batchResultId INTEGER , "
                + "configSettings BLOB , "
                + "currentState TINYTEXT , "
                + "description MEDIUMTEXT , "
                + "indexOfNextObject INTEGER , "
                + "isInIteration BIT NOT NULL , "
                + "objectQueryId INTEGER , "
                + "stateAfterIteration TINYTEXT , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL"
                + " )");
            database().executeSQL(
                "ALTER TABLE BatchJob ADD PRIMARY KEY (OID)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the BatchPlugin table, if needed.
     * @throws SQLException on error
     */
    private void createBatchPluginTable() throws SQLException
    {
        if (!database().hasTable("BatchPlugin"))
        {
            log.info("creating table BatchPlugin");

            database().executeSQL("CREATE TABLE BatchPlugin ("
                + "OID INTEGER NOT NULL , "
                + "authorId INTEGER , "
                + "batchEntity TINYTEXT , "
                + "configDescription BLOB , "
                + "defaultConfigSettings BLOB , "
                + "globalConfigSettings BLOB , "
                + "isPublished BIT NOT NULL , "
                + "lastModified DATETIME , "
                + "mainFileName TINYTEXT , "
                + "name TINYTEXT , "
                + "subdirName TINYTEXT , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL , "
                + "uploadedFileName TINYTEXT"
                + " )");
            database().executeSQL(
                "ALTER TABLE BatchPlugin ADD PRIMARY KEY (OID)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the BatchResult table, if needed.
     * @throws SQLException on error
     */
    private void createBatchResultTable() throws SQLException
    {
        if (!database().hasTable("BatchResult"))
        {
            log.info("creating table BatchResult");

            database().executeSQL("CREATE TABLE BatchResult ("
                + "OID INTEGER NOT NULL , "
                + "batchPluginId INTEGER , "
                + "completedTime DATETIME , "
                + "description MEDIUMTEXT , "
                + "isComplete BIT NOT NULL , "
                + "objectQueryId INTEGER , "
                + "userId INTEGER"
                + " )");
            database().executeSQL(
                "ALTER TABLE BatchResult ADD PRIMARY KEY (OID)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the BatchResultProperty table, if needed.
     * @throws SQLException on error
     */
    private void createBatchResultPropertyTable() throws SQLException
    {
        if (!database().hasTable("BatchResultProperty"))
        {
            log.info("creating table BatchResultProperty");
            database().executeSQL(
                "CREATE TABLE BatchResultProperty "
                + "(OID INTEGER NOT NULL , batchResultId INTEGER , "
                + "theIndex INTEGER , tag TINYTEXT , "
                + "contents BLOB , CUPDATEMUTABLEFIELDS BIT NOT NULL )");
            database().executeSQL(
                "ALTER TABLE BatchResultProperty ADD PRIMARY KEY (OID)");
        }
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(UpdateSet.class);
}
