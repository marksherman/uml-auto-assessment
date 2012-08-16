/*==========================================================================*\
 |  $Id: TokenDispenser.java,v 1.5 2011/12/25 21:18:24 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009-2011 Virginia Tech
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

package org.webcat.jobqueue;

import java.util.*;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * This class is used as a wait queue for worker threads, and allows
 * controlled dispensing of job tokens to worker threads.  A "token" is
 * nothing more than granting permission to execute.  This class is
 * based off the original GraderQueue class in the Grader
 * subsystem.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.5 $, $Date: 2011/12/25 21:18:24 $
 */
public class TokenDispenser
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Default constructor
     */
    public TokenDispenser(String name, int initialCount)
    {
        this.name = name;
        this.tokens = initialCount;
        log.debug("creating token dispenser: " + name());
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Get the name of this dispenser (primarily used for debugging/info
     * purposes).
     * @return This dispenser's name.
     */
    public String name()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the next available token, blocking until one is available.
     */
    public synchronized void getJobToken()
    {
        while (tokens == 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                log.error(name()
                    + ": client was interrupted while waiting for a token.");
            }
        }
        tokens--;
        if (log.isDebugEnabled())
        {
            log.debug(name() + ": releasing token to worker thread  ("
                + tokens + " tokens remain)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Add a token to the dispenser.
     */
    public synchronized void depositToken()
    {
        if (log.isDebugEnabled())
        {
            log.debug(name() + ": depositing one token");
        }
        tokens++;
        notify();
    }


    // ----------------------------------------------------------
    /**
     * Add multiple tokens to the dispenser.
     *
     * @param count The number of tokens to add.
     */
    public synchronized void depositTokens(long count)
    {
        if (log.isDebugEnabled())
        {
            log.debug(name() + ": depositing " + count + " tokens");
        }
        if (count > 0)
        {
            tokens += count;
            notify();
        }
    }


    // ----------------------------------------------------------
    /**
     * Add multiple tokens to the dispenser to ensure at least N are
     * available.
     *
     * @param N The minimum number of tokens to guarantee are present.
     *          If fewer are in the dispenser, the difference will be added.
     */
    public synchronized void ensureAtLeastNTokens(int n)
    {
        int amount = (n > tokens) ? (n - tokens) : 0;
        if (log.isDebugEnabled())
        {
            log.debug(name() + ": ensuring " + amount
                + " tokens (already holding " + tokens + " tokens)");
        }
        depositTokens(amount);
    }


    // ----------------------------------------------------------
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " " + name()
            + " [" + tokens + " tokens]";
    }


    //~ Instance/static variables .............................................

    int tokens;
    private String name;
    static Logger log = Logger.getLogger( TokenDispenser.class );
}
