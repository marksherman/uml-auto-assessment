/*==========================================================================*\
 |  $Id: Database.java,v 1.2 2010/09/27 00:21:13 stedwar2 Exp $
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

package org.webcat.dbupdate;

import com.webobjects.eoaccess.EOModel;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

// -------------------------------------------------------------------------
/**
 * An interface defining the methods that must be provided by a database
 * driver for the {@link UpdateEngine}.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:21:13 $
 */
public interface Database
{
    // ----------------------------------------------------------
    /**
     * Get the current database version for the given name, which is
     * usually associated with a specific subsystem/feature/EOModel.
     * @param subsystemName the unique name to look up
     * @return the version number of the current database schema associated
     * with this name, or -1 if none is found.
     * @throws SQLException when an error occurs
     */
    int currentVersionNumber( String subsystemName ) throws SQLException;

    // ----------------------------------------------------------
    /**
     * Set the current database version for the given name, which is
     * usually associated with a specific subsystem/feature/EOModel.
     * @param subsystemName the unique name to use
     * @param aVersionNumber the version number to set
     * @throws SQLException when an error occurs
     */
    void setVersionNumber( String subsystemName, int aVersionNumber )
        throws SQLException;

    // ----------------------------------------------------------
    /**
     * @throws SQLException when an error occurs
     */
    void initializeVersionTable() throws SQLException;

    // ----------------------------------------------------------
    /**
     * Check to see if a specific table exists in the database.
     * @param tableName the name of the table to check for
     * @param columnName the name of a column appearing in the table
     * @param value the name of a value legal for the column
     * @return true if the table exists
     */
    boolean hasTable( String tableName, String columnName, String value );

    // ----------------------------------------------------------
    /**
     * Check to see if a specific table exists in the database.
     * @param tableName the name of the table to check for
     * @return true if the table exists
     */
    boolean hasTable( String tableName );

    // ----------------------------------------------------------
    /**
     * Check to see if the version information table exists in the database.
     * @return true if the version table exists
     */
    boolean hasVersionTable();

    // ----------------------------------------------------------
    /**
     * Check to see if a lock associated with the given subsystem name is
     * present in the version information table.
     * @param subsystemName the name to look up
     * @return true if the version table contains a lock for this name
     */
    boolean isLocked( String subsystemName );

    // ----------------------------------------------------------
    /**
     * Set a lock associated with the given subsystem name in the version
     * information table.
     * @param subsystemName the name to look up
     * @return true if the lock is acquired
     */
    boolean tryToLock( String subsystemName );

    // ----------------------------------------------------------
    /**
     * Remove the lock associated with the given subsystem name in the version
     * information table.
     * @param subsystemName the name to look up
     * @throws SQLException when an error occurs
     */
    void unlock( String subsystemName ) throws SQLException;

    // ----------------------------------------------------------
    /**
     * Execute raw SQL on the database.
     * @param anSQLString the SQL to execute
     * @return the result
     * @throws SQLException when an error occurs
     */
    Statement executeSQLWithResult( String anSQLString ) throws SQLException;

    // ----------------------------------------------------------
    /**
     * Execute raw SQL on the database.
     * @param anSQLString the SQL to execute
     * @throws SQLException when an error occurs
     */
    void executeSQL( String anSQLString ) throws SQLException;

    // ----------------------------------------------------------
    /**
     * Close the database connection.
     */
    void close();

    // ----------------------------------------------------------
    /**
     * Get the user name that will be used to establish the JDBC connection.
     * @return the user name
     */
    String userName();

    // ----------------------------------------------------------
    /**
     * Set the user name that will be used to establish the JDBC connection.
     * @param value the user name
     */
    void setUserName( String value );

    // ----------------------------------------------------------
    /**
     * Get the password that will be used to establish the JDBC connection.
     * @return the password
     */
    String password();

    // ----------------------------------------------------------
    /**
     * Set the password that will be used to establish the JDBC connection.
     * @param value the password
     */
    void setPassword( String value );

    // ----------------------------------------------------------
    /**
     * Get the connection URL that will be used to establish the JDBC
     * connection.
     * @return the connection URL
     */
    String connectionUrlString();

    // ----------------------------------------------------------
    /**
     * Set the connection URL that will be used to establish the JDBC
     * connection.
     * @param value the connection URL
     */
    void setConnectionUrlString( String value );

    // ----------------------------------------------------------
    /**
     * Get the table name used for the version information table.
     * @return the table name
     */
    String versionTableName();

    // ----------------------------------------------------------
    /**
     * Set the table name used for the version information table.
     * @param value the table name
     */
    void setVersionTableName( String value );

    // ----------------------------------------------------------
    /**
     * Get the class name of the JDBC driver to use.
     * @return the class name
     */
    String driverClassName();

    // ----------------------------------------------------------
    /**
     * Set the class name of the JDBC driver to use.
     * @param value the class name
     */
    void setDriverClassName( String value );

    // ----------------------------------------------------------
    /**
     * Set the connection information from the given EOModel.
     * @param model the model to read from
     */
    void setConnectionInfoFromEOModel( EOModel model );

    // ----------------------------------------------------------
    /**
     * Set the connection information from properties.
     * @param properties the properties to read from
     */
    void setConnectionInfoFromProperties( Properties properties );

    // ----------------------------------------------------------
    /**
     * Ensure the database driver class is loaded.
     */
    void loadDatabaseDriver();

    // ----------------------------------------------------------
    /**
     * The default version table name.
     */
    static final String DEFAULT_VERSION_TABLE_NAME = "DBVERSION";

    // ----------------------------------------------------------
    /**
     * The default JDBC driver class name.
     */
    static final String DEFAULT_DRIVER_CLASS_NAME =
        "org.gjt.mm.mysql.Driver";

}
