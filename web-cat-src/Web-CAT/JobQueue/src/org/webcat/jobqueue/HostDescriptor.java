/*==========================================================================*\
 |  $Id: HostDescriptor.java,v 1.3 2011/12/25 21:18:24 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008-2011 Virginia Tech
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

import org.webcat.core.Application;
import org.webcat.woextensions.WCEC;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

// -------------------------------------------------------------------------
/**
 * Represents and identifies a Web-CAT host within the cluster of
 * servers operating on a single shared database.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/12/25 21:18:24 $
 */
public class HostDescriptor
    extends _HostDescriptor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new HostDescriptor object.
     */
    public HostDescriptor()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Get a local instance of the current host's host descriptor in
     * the given editing context.
     * @return The descriptor.
     */
    public static HostDescriptor currentHost(EOEditingContext context)
    {
        ensureCurrentHostIsRegistered();
        return (HostDescriptor)currentHost.localInstanceIn(context);
    }


    // ----------------------------------------------------------
    /**
     * Create a new managed descriptor for the current host, registering
     * it if necessary.
     * @return The managed descriptor.
     */
    public static ManagedHostDescriptor newHostDescriptor(
        EOEditingContext context)
    {
        return new ManagedHostDescriptor(
            registerHost(context, canonicalHostName()));
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Get the current host's canonical name.
     * @return The canonical name for the current host.
     */
    public static String canonicalHostName()
    {
        if (canonicalHostName == null)
        {
            try
            {
                java.net.InetAddress localMachine =
                    java.net.InetAddress.getLocalHost();
                canonicalHostName = localMachine.getCanonicalHostName();
            }
            catch (java.net.UnknownHostException e)
            {
                log.error("Error looking up local host info: " + e);
                canonicalHostName = "unknown";
            }
            log.info("canonical host name = " + canonicalHostName);
        }
        return canonicalHostName;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Registers a host in the database, if it has not already been
     * registered.
     * @param context The editing context to use.
     * @param hostName The name of the host.
     * @return The registered descriptor.
     */
    /* protected */ static void ensureCurrentHostIsRegistered()
    {
        if (currentHost == null)
        {
            // TODO: This needs to be fixed regarding the EC used
            currentHost =
                newHostDescriptor(WCEC.newAutoLockingEditingContext());
        }
    }


    // ----------------------------------------------------------
    /**
     * Registers a host in the database, if it has not already been
     * registered.
     * @param context The editing context to use.
     * @param forHostName The name of the host.
     * @return The registered descriptor.
     */
    private static HostDescriptor registerHost(
        EOEditingContext context, String forHostName)
    {
        return (HostDescriptor)JobQueue.registerDescriptor(
            context,
            ENTITY_NAME,
            new NSDictionary<String, String>(
                forHostName,
                HOST_NAME_KEY),
            null);
    }


    //~ Instance/static variables .............................................

    private static String canonicalHostName;
    private static ManagedHostDescriptor currentHost;
}
