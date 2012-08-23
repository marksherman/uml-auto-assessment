/*==========================================================================*\
 |  $Id: UpdateSet.java,v 1.3 2010/10/14 18:41:45 stedwar2 Exp $
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

import java.lang.reflect.Method;
import java.sql.*;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * Encapsulates the necessary database schema updates necessary to bring
 * any older version of an application's database up to the most current
 * supported version.  Normally, one concrete subclass would be created
 * for each EOModel or "subsystem" that is managed separately.  The intent
 * is for one subclass to contain information about all possible updates for
 * the given EOModel or subsystem, with each version x to version x+1
 * transformation isolated in a single method named according to a given
 * naming convention.
 *
 * By convention, a subclass method named updateToVersionX() would take
 * a database currently at version X-1 and apply the necessary changes to
 * bring it up to version X.  Any older version Y could be upgraded by
 * successively applying each Y+1, Y+2, Y+3, ..., X-1, X transformation
 * in sequence.
 *
 * @author Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2010/10/14 18:41:45 $
 */
public abstract class UpdateSet
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new UpdateSet object with the given name.  The name is
     * a unique identifier for this update set, associated with either the
     * EOModel(s) or subsystem(s) to which the updates apply.  Version
     * numbers for different names are maintained separately.
     * @param subsystemName the unique identifier for this update set.
     */
    public UpdateSet(String subsystemName)
    {
        name = subsystemName;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Get the unique identifier for this update set.
     * @return the unique name
     */
    public String subsystemName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Get the database to which updates will be applied.
     * @return the database
     */
    public Database database()
    {
        return database;
    }


    // ----------------------------------------------------------
    /**
     * Set the database to which updates will be applied.
     * @param database the database to operate on
     */
    public void setDatabase(Database database)
    {
        this.database = database;
    }


    // ----------------------------------------------------------
    /**
     * Get the old version of the database before applying updates.
     * @return the old database version
     */
    public int startingVersion()
    {
        return startingVersion;
    }


    // ----------------------------------------------------------
    /**
     * Set the old version of the database before applying updates.
     * @param version the old database version
     */
    public void setStartingVersion(int version)
    {
        startingVersion = version;
    }


    // ----------------------------------------------------------
    /**
     * Determines whether or not this update set is compatible with the
     * given version number.  This method is used as a way to determine
     * when the database has already been updated by an application using
     * a newer (potentially incompatible) update set than this one.
     *
     * @param version The version to check
     * @return true if the given version is recognized by this update set
     */
    public boolean supportsVersion(int version)
    {
        return version < 0 || methodForVersion(version) != null;
    }


    // ----------------------------------------------------------
    /**
     * Apply the update necessary to upgrade to the given version.
     * @param version The version to move to
     * @return true if the update is applied, false if no such update exists
     */
    public boolean applyUpdateIncrement(int version)
    {
        Method  update = methodForVersion(version);
        boolean result = false;
        if (update != null)
        {
            try
            {
                log.info("applying " + subsystemName()
                          + " database update " + version);
                update.invoke(this);
                result = true;
            }
            catch (Exception e)
            {
                log.error("exception trying to update '" + subsystemName()
                    + "' to version " + version, e);
                throw new com.webobjects.foundation.NSForwardException(e);
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Applies any necessary updates from the given set in order to bring
     * the database up to the most recent version.  The version 0 method
     * should bring the database up to a baseline state, creating any
     * initial tables and so on.  It is declared abstract here to make
     * sure that every update set provides at least version 0 support.
     * @throws SQLException
     */
    public abstract void updateIncrement0() throws SQLException;


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    /**
     * Creates an index for the specified column in the given table.
     * @param tableName The name of the table to index
     * @param columnName The name of the column to index on--add a length
     *                   specification in parentheses (SQL-style) for
     *                   text columns.
     */
    protected void createIndexFor(String tableName, String columnName)
    {
        log.info("creating index for table "
            + tableName + " on column " + columnName);
        try
        {
            database().executeSQL("alter table " + tableName
                + " add index (" + columnName + ")");
        }
        catch (SQLException e)
        {
            log.error("Unable to create index in table "
                + tableName + " for column spec " + columnName
                + ": " + e.getClass() + ": " + e);
        }
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Uses reflection to find the method used to update from version
     * v-1 to version v.
     * @param version The version to update to
     * @return the method object, or null if no such method exists
     */
    private Method methodForVersion(int version)
    {
        try
        {
            return this.getClass().getMethod("updateIncrement" + version);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    //~ Instance/static variables .............................................

    private String   name;
    private Database database;
    private int      startingVersion = -1;

    protected static Logger log = Logger.getLogger(UpdateSet.class);
}
