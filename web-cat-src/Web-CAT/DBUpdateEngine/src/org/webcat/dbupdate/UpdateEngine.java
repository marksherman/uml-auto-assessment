/*==========================================================================*\
 |  $Id: UpdateEngine.java,v 1.2 2010/09/27 00:21:13 stedwar2 Exp $
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

import java.sql.SQLException;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * An engine that automatically applies necessary updates to a database
 * schema in order to bring it up to date.  This class is a singleton.
 * The core idea for this framework was inspired by a stepwise article by
 * James Seigel (wo_james@paddlethis.com) and Christian Pekeler
 * (christian@pekeler.org):
 *
 * http://www.stepwise.com/Articles/2005/DBChanges/index.html
 *
 * The original article's code was also modified and posted on WOCode by
 * Helge Staedtler (lgxxl@web.de) in July 2005:
 *
 * http://wocode.com/cgi-bin/WebObjects/WOCode.woa/2/wa/ShareCodeItem?itemId=424
 *
 * The code in this framework uses ideas from both of these sources, together
 * with some additional redesign work.
 *
 * @author Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:21:13 $
 */
public final class UpdateEngine
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new UpdateEngine object.  The constructor is private
     * because this is a singleton class.
     */
    private UpdateEngine()
    {
        // everything is initialized lazily, on first use
    }

    // ----------------------------------------------------------
    /**
     * Returns the singleton instance of this class.
     * @return the engine
     */
    public static UpdateEngine instance()
    {
        if( engine == null )
        {
            engine = new UpdateEngine();
        }
        return engine;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Applies any necessary updates from the given set in order to bring
     * the database up to the most recent version.
     *
     * @param updates The collection of updates to apply
     */
    public void applyNecessaryUpdates( UpdateSet updates )
    {
        try
        {
            if ( !database().hasVersionTable() )
            {
                database().initializeVersionTable();
            }
            if ( database.tryToLock( updates.subsystemName() ) )
            {
                // here all the action takes place...
                try
                {
                    executeUpdates( updates );
                }
                finally
                {
                    database.unlock( updates.subsystemName() );
                }
            }
            else {
                waitUntilDBUnlocked( updates.subsystemName() );
            }
        }
        catch ( RuntimeException e )
        {
            log.error( "updates for " + updates.subsystemName() + " failed",
                       e );
            throw e;
        }
        catch ( Exception e )
        {
            log.error( "updates for " + updates.subsystemName() + " failed",
                       e );
            throw new com.webobjects.foundation.NSForwardException( e );
        }
        finally
        {
            database.close();
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the database to which updates will be applied.
     * @return the database
     */
    public Database database()
    {
        if ( database == null )
        {
            database = new MySQLDatabase();
        }
        return database;
    }


    // ----------------------------------------------------------
    /**
     * Set the database to which updates will be applied.
     * @param database the database to operate on
     */
    public void setDatabase( Database database )
    {
        this.database = database;
    }


    //~ Private Methods .......................................................

    protected void executeUpdates( UpdateSet updates )
        throws Exception
    {
        int latestVersion = database.currentVersionNumber(
                        updates.subsystemName() );
        String name = updates.subsystemName();
        if ( updates.supportsVersion( latestVersion ) )
        {
            updates.setDatabase( database() );
            updates.setStartingVersion( latestVersion );
            latestVersion++;
            while ( updates.applyUpdateIncrement( latestVersion ) )
            {
                log.info( "updated " + name + " to v." + latestVersion );
                database.setVersionNumber( name, latestVersion );
                latestVersion++;
            }
        }
        else
        {
            Exception e = new IllegalStateException(
                "illegal database version " + latestVersion
                + " for subsystem " + updates.subsystemName() );
            log.fatal( "unsupported database version", e );
            throw e;
        }
    }


    protected void waitUntilDBUnlocked(String subsystemName)
    {
        int waitCount = 0;
        do
        {
            log.info("waiting while another application instance "
                     + "performs DB updates");
            try
            {
                Thread.sleep(5 * 1000);
            }
            catch (InterruptedException exception)
            {
                // do nothing
            }
            waitCount++;
        }
        while (database().isLocked(subsystemName) && waitCount <= WAIT_LIMIT);
        if (database().isLocked(subsystemName))
        {
            // Give up and try to forcibly unlock, assuming that
            // the original lock holder is dead
            try
            {
                database().unlock(subsystemName);
            }
            catch (SQLException e)
            {
                log.error(
                    "Unable to remove stale lock for " + subsystemName, e);
            }
        }
    }


    //~ Instance/static variables .............................................

    private Database database;
    private static UpdateEngine engine;

    private static final int WAIT_LIMIT = 6;

    static Logger log = Logger.getLogger( UpdateEngine.class );
}
