/*==========================================================================*\
 |  $Id: UpdateSetTest.java,v 1.2 2010/09/27 00:21:13 stedwar2 Exp $
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.webcat.dbupdate.*;

// -------------------------------------------------------------------------
/**
 * A simple test class to try out the basic methods of {@link UpdateSet}.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:21:13 $
 */
public class UpdateSetTest
    extends junit.framework.TestCase
{
    // ----------------------------------------------------------
    protected void setUp()
    {
        updateSet = new MockUpdateSet();
        database  = new MockDatabase();
        updateSet.setDatabase( database );
    }

    public void testSupportsVersion()
    {
        assertTrue( updateSet.supportsVersion( -1 ) );
        assertTrue( updateSet.supportsVersion( 0 ) );
        assertTrue( updateSet.supportsVersion( 1 ) );
        assertTrue( updateSet.supportsVersion( 2 ) );
        assertFalse( updateSet.supportsVersion( 3 ) );
        assertFalse( updateSet.supportsVersion( 100 ) );
    }

    public void testUpdate0()
    {
        assertTrue( updateSet.applyUpdateIncrement( 0 ) );
        assertEquals( " v0", database.history );
    }

    public void testUpdate2()
    {
        assertTrue( updateSet.applyUpdateIncrement( 0 ) );
        assertTrue( updateSet.applyUpdateIncrement( 1 ) );
        assertTrue( updateSet.applyUpdateIncrement( 2 ) );
        assertFalse( updateSet.applyUpdateIncrement( 3 ) );
        assertEquals( " v0 v1 v2", database.history );
    }


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


    static class MockDatabase
        extends MySQLDatabase
    {
        public Statement executeSQLWithResult( String anSQLString )
        {
            history += " " + anSQLString;
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
    }


    //~ Instance/static variables .............................................

    private UpdateSet    updateSet;
    private MockDatabase database;
}
