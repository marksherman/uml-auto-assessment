/*==========================================================================*\
 |  $Id: UpdateEngineTest.java,v 1.2 2010/09/27 00:21:13 stedwar2 Exp $
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

package org.webcat.dbupdate.tests;

import com.webobjects.eoaccess.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.webcat.dbupdate.*;

// -------------------------------------------------------------------------
/**
 * A simple test class to try out the basic methods of {@link UpdateEngine}.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:21:13 $
 */
public class UpdateEngineTest
    extends junit.framework.TestCase
{
    // ----------------------------------------------------------
    protected void setUp()
    {
        updates = new MockUpdateSet();
        database  = new MockDatabase();
        UpdateEngine.instance().setDatabase( database );
    }


    // ----------------------------------------------------------
    public void testUpdateFromEmpty() throws SQLException
    {
        database.tablesExist = false;
        UpdateEngine.instance().applyNecessaryUpdates( updates );
        assertEquals(
            2, database.currentVersionNumber( updates.subsystemName() ) );
        assertEquals(
            " hasTable(DBVERSION) initializeVersionTable tryToLock(mock)"
            + " executeSQL(v0) executeSQL(v1) executeSQL(v2) unlock(mock)"
            + " close",
            database.history );
    }


    // ----------------------------------------------------------
    public void testUpdateFrom0() throws SQLException
    {
        database.setVersionNumber( updates.subsystemName(), 0 );
        UpdateEngine.instance().applyNecessaryUpdates( updates );
        assertEquals(
            2, database.currentVersionNumber( updates.subsystemName() ) );
        assertEquals(
            " hasTable(DBVERSION) tryToLock(mock)"
            + " executeSQL(v1) executeSQL(v2) unlock(mock) close",
            database.history );
    }


    // ----------------------------------------------------------
    public void testUpdateFrom1() throws SQLException
    {
        database.setVersionNumber( updates.subsystemName(), 1 );
        UpdateEngine.instance().applyNecessaryUpdates( updates );
        assertEquals(
            2, database.currentVersionNumber( updates.subsystemName() ) );
        assertEquals(
            " hasTable(DBVERSION) tryToLock(mock)"
            + " executeSQL(v2) unlock(mock) close",
            database.history );
    }


    // ----------------------------------------------------------
    public void testUpdateFrom2() throws SQLException
    {
        database.setVersionNumber( updates.subsystemName(), 2 );
        UpdateEngine.instance().applyNecessaryUpdates( updates );
        assertEquals(
            2, database.currentVersionNumber( updates.subsystemName() ) );
        assertEquals(
            " hasTable(DBVERSION) tryToLock(mock)"
            + " unlock(mock) close",
            database.history );
    }


    // ----------------------------------------------------------
    public void testUpdateFrom3() throws SQLException
    {
        database.setVersionNumber( updates.subsystemName(), 3 );
        try
        {
            UpdateEngine.instance().applyNecessaryUpdates( updates );
            fail();
        }
        catch ( IllegalStateException expected )
        {
            // should be thrown because of upsupported version number
        }
    }


    // ----------------------------------------------------------
    public static class MockUpdateSet
        extends UpdateSet
    {
        public MockUpdateSet()
        {
            super( "mock" );
        }

        public void updateIncrement0() throws SQLException
        {
            database().executeSQL( "v0" );
        }

        public void updateIncrement1() throws SQLException
        {
            database().executeSQL( "v1" );
        }

        public void updateIncrement2() throws SQLException
        {
            database().executeSQL( "v2" );
        }
    }


    // ----------------------------------------------------------
    static class MockDatabase
        extends MySQLDatabase
    {
        public int currentVersionNumber( String subsystemName )
            throws SQLException
        {
            Object result = versionMap.get( subsystemName );
            if ( result == null )
            {
                return -1;
            }
            else
            {
                return ( (Number)result ).intValue();
            }
        }

        @SuppressWarnings( "unchecked" )
        public void setVersionNumber( String subsystemName, int aVersionNumber )
            throws SQLException
        {
            versionMap.put( subsystemName, new Integer( aVersionNumber ) );
        }

        public void initializeVersionTable() throws SQLException
        {
            history += " initializeVersionTable";
        }

        public boolean hasTable(
            String tableName, String columnName, String value )
        {
            history += " hasTable(" + tableName + ")";
            return tablesExist;
        }

        public boolean isLocked( String subsystemName )
        {
            history += " isLocked(" + subsystemName + ")";
            return true;
        }

        public boolean tryToLock( String subsystemName )
        {
            history += " tryToLock(" + subsystemName + ")";
            return true;
        }

        public void unlock( String subsystemName ) throws SQLException
        {
            history += " unlock(" + subsystemName + ")";
        }

        public void close()
        {
            history += " close";
        }

        public Statement executeSQLWithResult( String anSQLString )
        {
            history += " executeSQL(" + anSQLString + ")";
            return null;
        }

        public void executeSQL( String anSQLString )
        {
            executeSQLWithResult( anSQLString );
        }

        protected Connection connection()
        {
            throw new RuntimeException();
        }

        String history = "";
        boolean tablesExist = true;
        Map     versionMap  = new java.util.TreeMap();
    }


    //~ Instance/static variables .............................................

    private UpdateSet    updates;
    private MockDatabase database;
}
