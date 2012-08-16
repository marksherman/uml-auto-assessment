/*==========================================================================*\
 |  $Id: MySQLDatabase.java,v 1.2 2010/09/27 00:21:13 stedwar2 Exp $
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

import com.webobjects.eoaccess.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * A default implementation of the {@link Database} interface that works
 * for MySQL (and some others).  If this concrete subclass doesn't work
 * for you, consider subclassing it and overriding whatever is necessary.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:21:13 $
 */
public class MySQLDatabase
    implements Database
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new MySQLDatabase object.
     */
    public MySQLDatabase()
    {
        // Everything is initialized lazily
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Get the current database version for the given name, which is
     * usually associated with a specific subsystem/feature/EOModel.
     * @param subsystemName the unique name to look up
     * @return the version number of the current database schema associated
     * with this name, or -1 if none is found.
     * @throws SQLException when an error occurs
     */
    public int currentVersionNumber( String subsystemName )
        throws SQLException
    {
        Statement update = executeSQLWithResult(
            "SELECT version FROM " + versionTableName()
            + " WHERE subsystem = '" + subsystemName + "'" );
        ResultSet resultSet = update.getResultSet();
        if ( resultSet.next() )
        {
            int result = resultSet.getInt( 1 );
            update.close();
            return result;
        }
        else
        {
            update.close();
            initializeSubsystemRow( subsystemName, 0 );
            return -1;
        }
    }


    // ----------------------------------------------------------
    /**
     * Set the current database version for the given name, which is
     * usually associated with a specific subsystem/feature/EOModel.
     * @param subsystemName the unique name to use
     * @param aVersionNumber the version number to set
     * @throws SQLException when an error occurs
     */
    public void setVersionNumber( String subsystemName, int aVersionNumber )
        throws SQLException
    {
        executeSQL( "UPDATE " + versionTableName() + " SET version = "
                    + aVersionNumber + " WHERE subsystem = '"
                    + subsystemName + "'" );
    }


    // ----------------------------------------------------------
    /**
     * @throws SQLException when an error occurs
     */
    public void initializeVersionTable()
        throws SQLException
    {
        try
        {
            log.info( "No version table present.  Creating table." );
            executeSQL(
                "CREATE TABLE " + versionTableName()
                + " (subsystem TINYTEXT NOT NULL, "
                + "update_lock BIT NOT NULL, "
                + "version INTEGER NOT NULL)" );
        } catch( SQLException e ) {
            log.fatal( "error creating version table", e );
            throw e;
        }
    }


    // ----------------------------------------------------------
    /**
     * Check to see if a specific table exists in the database.
     * @param tableName the name of the table to check for
     * @param columnName the name of a column appearing in the table
     * @param value the name of a value legal for the column
     * @return true if the table exists
     */
    public boolean hasTable( String tableName,
                             String columnName,
                             String value )
    {
        try
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "checking for table: " + tableName + ", "
                           + columnName + ", " + value );
            }
            String cmd = "SELECT " + columnName + " FROM " + tableName;
            if ( value != null )
            {
                cmd += " WHERE " + columnName + " = '" + value + "'";
            }
            executeSQL( cmd );
            return true;
        }
        catch ( SQLException e )
        {
            return false; // table does not exist
        }
    }

    // ----------------------------------------------------------
    /**
     * Check to see if a specific table exists in the database.
     * @param tableName the name of the table to check for
     * @return true if the table exists
     */
    public boolean hasTable( String tableName )
    {
        return hasTable( tableName, "oid", "1" );
    }


    // ----------------------------------------------------------
    /**
     * Check to see if the version information table exists in the database.
     * @return true if the version table exists
     */
    public boolean hasVersionTable()
    {
        return hasTable( versionTableName, "update_lock", "1" );
    }


    // ----------------------------------------------------------
    /**
     * Check to see if a lock associated with the given subsystem name is
     * present in the version information table.
     * @param subsystemName the name to look up
     * @return true if the version table contains a lock for this name
     */
    public boolean isLocked( String subsystemName )
    {
        try
        {
            Statement update = executeSQLWithResult(
                "SELECT update_lock FROM " + versionTableName()
                + " WHERE subsystem = '" + subsystemName + "'" );
            ResultSet resultSet = update.getResultSet();
            resultSet.next();
            boolean result = resultSet.getInt( 1 ) == 1;
            update.close();
            return result;
        }
        catch ( SQLException e )
        {
            log.error( "SQL error testing lock", e );
            return true;
        }
    }


    // ----------------------------------------------------------
    /**
     * Set a lock associated with the given subsystem name in the version
     * information table.
     * @param subsystemName the name to look up
     * @return true if the lock is acquired
     */
    public boolean tryToLock( String subsystemName )
    {
        log.debug( "attempting to lock database for " + subsystemName );
        try
        {
            Statement update = executeSQLWithResult(
                "SELECT update_lock FROM " + versionTableName()
                + " WHERE subsystem = '" + subsystemName + "'" );
            ResultSet resultSet = update.getResultSet();
            if ( resultSet.next() )
            {
                if ( resultSet.getInt( 1 ) == 1 )
                {
                    update.close();
                    return false;
                }
                else
                {
                    update = executeSQLWithResult(
                        "UPDATE " + versionTableName()
                        + " SET update_lock = 1 WHERE subsystem = '"
                        + subsystemName + "'");
                    boolean result = update.getUpdateCount() == 1;
                    update.close();
                    return result;
                }
            }
            else
            {
                try
                {
                    initializeSubsystemRow( subsystemName, 1 );
                    return true;
                }
                catch ( SQLException e )
                {
                    log.fatal( "failure to insert new row for "
                               + subsystemName, e );
                    return false;
                }
            }
        }
        catch ( SQLException e )
        {
            log.fatal( "unexpected SQLException trying to lock "
                       + subsystemName, e );
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * Remove the lock associated with the given subsystem name in the version
     * information table.
     * @param subsystemName the name to look up
     * @throws SQLException when an error occurs
     */
    public void unlock( String subsystemName )
        throws SQLException
    {
        log.debug( "unlocking database for " + subsystemName );
        executeSQL( "UPDATE " + versionTableName() + " SET update_lock = 0 "
                    + "WHERE subsystem = '" + subsystemName + "'" );
    }


    // ----------------------------------------------------------
    /**
     * Execute raw SQL on the database.
     * @param anSQLString the SQL to execute
     * @return the result
     * @throws SQLException when an error occurs
     */
    public Statement executeSQLWithResult( String anSQLString )
        throws SQLException
    {
        log.debug( "executeSQL: " + anSQLString );
        Statement update = connection().createStatement();
        update.execute( anSQLString );
        return update;
    }


    // ----------------------------------------------------------
    /**
     * Execute raw SQL on the database.
     * @param anSQLString the SQL to execute
     * @throws SQLException when an error occurs
     */
    public void executeSQL( String anSQLString )
        throws SQLException
    {
        executeSQLWithResult( anSQLString ).close();
    }


    // ----------------------------------------------------------
    /**
     * Close the database connection.
     */
    public void close()
    {
        if ( connection != null )
        {
            try
            {
                connection.close();
                connection = null;
            }
            catch ( SQLException dbNotClosingSQLException )
            {
                log.error( "Failed to close JDBC connection ",
                           dbNotClosingSQLException );
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the user name that will be used to establish the JDBC connection.
     * @return the user name
     */
    public String userName()
    {
        return userName;
    }


    // ----------------------------------------------------------
    /**
     * Set the user name that will be used to establish the JDBC connection.
     * @param value the user name
     */
    public void setUserName( String value )
    {
        userName = value;
    }


    // ----------------------------------------------------------
    /**
     * Get the password that will be used to establish the JDBC connection.
     * @return the password
     */
    public String password()
    {
        return password;
    }


    // ----------------------------------------------------------
    /**
     * Set the password that will be used to establish the JDBC connection.
     * @param value the password
     */
    public void setPassword( String value )
    {
        password = value;
    }


    // ----------------------------------------------------------
    /**
     * Get the connection URL that will be used to establish the JDBC
     * connection.
     * @return the connection URL
     */
    public String connectionUrlString()
    {
        return connectionUrlString;
    }


    // ----------------------------------------------------------
    /**
     * Set the connection URL that will be used to establish the JDBC
     * connection.
     * @param value the connection URL
     */
    public void setConnectionUrlString( String value )
    {
        connectionUrlString = value;
    }


    // ----------------------------------------------------------
    /**
     * Get the table name used for the version information table.
     * @return the table name
     */
    public String versionTableName()
    {
        return versionTableName;
    }


    // ----------------------------------------------------------
    /**
     * Set the table name used for the version information table.
     * @param value the table name
     */
    public void setVersionTableName( String value )
    {
        versionTableName = value;
    }


    // ----------------------------------------------------------
    /**
     * Get the class name of the JDBC driver to use.
     * @return the class name
     */
    public String driverClassName()
    {
        return driverClassName;
    }


    // ----------------------------------------------------------
    /**
     * Set the class name of the JDBC driver to use.
     * @param value the class name
     */
    public void setDriverClassName( String value )
    {
        driverClassName = value;
    }


    // ----------------------------------------------------------
    /**
     * Set the connection information from the given EOModel.
     * @param model the model to read from
     */
    public void setConnectionInfoFromEOModel( EOModel model )
    {
        setUserName( (String)model.connectionDictionary()
                        .objectForKey( "username" ) );
        setPassword( (String)model.connectionDictionary()
                        .objectForKey( "password" ) );
        setConnectionUrlString( (String)model.connectionDictionary()
                        .objectForKey( "URL" ) );
        setDriverClassName( (String)model.connectionDictionary()
                        .objectForKey( "driver" ) );
    }


    // ----------------------------------------------------------
    /**
     * Set the connection information from properties.
     * @param properties the properties to read from
     */
    public void setConnectionInfoFromProperties( Properties properties )
    {
        String value = properties.getProperty( "dbConnectAdmin" );
        if ( value == null )
        {
            value = properties.getProperty( "dbConnectUserGLOBAL" );
        }
        if ( value != null )
        {
            setUserName( value );
        }
        value = properties.getProperty( "dbConnectAdminPassword" );
        if ( value == null )
        {
            value = properties.getProperty( "dbConnectPasswordGLOBAL" );
        }
        if ( value != null )
        {
            setPassword( value );
        }
        value = properties.getProperty( "dbConnectURLGLOBAL" );
        if ( value != null )
        {
            setConnectionUrlString( value );
        }
        value = properties.getProperty( "dbConnectDriverGLOBAL" );
        if ( value != null )
        {
            setDriverClassName( value );
        }
    }


    // ----------------------------------------------------------
    /**
     * Ensure the database driver class is loaded.
     */
    public void loadDatabaseDriver()
    {
        try
        {
            Class.forName( driverClassName() );
        }
        catch ( Exception e )
        {
            String msg = "Unable to load driver for database '"
                + driverClassName() + "'";
            log.fatal( msg );
            throw new com.webobjects.foundation.NSForwardException( e, msg );
        }
    }


    //~ Protected Methods .....................................................

    protected void initializeSubsystemRow( String subsystemName, int lock )
        throws SQLException
    {
        executeSQL(
            "INSERT INTO " + versionTableName()
            + " (subsystem, update_lock, version) VALUES ('" + subsystemName
            + "', " + lock + ", -1)" );
    }


    protected Connection connection()
    {
        if ( connection == null )
        {
            try
            {
                loadDatabaseDriver();
                connection = DriverManager.getConnection(
                                connectionUrlString, userName, password );
            }
            catch ( SQLException e )
            {
                String msg =
                    "Failed to connect to the database using the update user.";
                log.fatal( msg );
                throw new com.webobjects.foundation.NSForwardException( e,
                                                                        msg );
            }
        }
        return connection;
    }


    //~ Instance/static variables .............................................

    protected String versionTableName = DEFAULT_VERSION_TABLE_NAME;
    protected String driverClassName = DEFAULT_DRIVER_CLASS_NAME;
    protected String connectionUrlString;
    protected String userName;
    protected String password;
    protected Connection connection;

    static Logger log = Logger.getLogger( MySQLDatabase.class );
}
