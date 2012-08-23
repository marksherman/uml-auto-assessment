/*==========================================================================*\
 |  $Id: ProtocolRegistry.java,v 1.1 2010/03/02 18:38:52 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter;

import java.util.Hashtable;
import java.util.Map;

import org.webcat.submitter.internal.protocols.FileProtocol;
import org.webcat.submitter.internal.protocols.FtpProtocol;
import org.webcat.submitter.internal.protocols.HttpProtocol;
import org.webcat.submitter.internal.protocols.HttpsProtocol;
import org.webcat.submitter.internal.protocols.MailtoProtocol;

//--------------------------------------------------------------------------
/**
 * Manages the transport protocols that are registered for use by the
 * submitter.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:52 $
 */
public class ProtocolRegistry
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes the protocol registry and registers the protocols that are
     * built-in to the submitter.
     */
    private ProtocolRegistry()
    {
        protocolTypes = new Hashtable<String, Class<? extends IProtocol>>();

        // Register the built-in handlers.

        add("http", HttpProtocol.class);
        add("https", HttpsProtocol.class);
        add("file", FileProtocol.class);
        add("ftp", FtpProtocol.class);
        add("mailto", MailtoProtocol.class);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the global instance of the ProtocolRegistry.
     *
     * @return the global instance of the ProtocolRegistry
     */
    public static ProtocolRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new ProtocolRegistry();
        }

        return instance;
    }


    // ----------------------------------------------------------
    /**
     * <p>
     * Adds a protocol handler to the registry so that it can be referenced in
     * the submission definitions.
     * </p><p>
     * If a protocol is added with the same scheme as one that is already
     * registered, the new protocol handler will replace the existing one. This
     * allows users to replace the functionality of the built-in protocols
     * (http, https, ftp, and mailto) if they wish.
     * </p>
     *
     * @param scheme the URI scheme of the protocol to register
     * @param type the type of the protocol handler to be associated with this
     *     scheme, which must implement the {@link IProtocol} interface
     */
    public void add(String scheme, Class<? extends IProtocol> type)
    {
        protocolTypes.put(scheme, type);
    }


    // ----------------------------------------------------------
    /**
     * Called by the submitter internally to create a new instance of the
     * protocol handler with the specified URI scheme.
     *
     * @param scheme the URI scheme of the protocol handler to create
     * @returns an instance of the requested protocol handler, or null if no
     *     protocol handler with this URI scheme was registered
     */
    /*package*/ IProtocol createProtocolInstance(String scheme)
    {
        if (protocolTypes.containsKey(scheme))
        {
            Class<? extends IProtocol> type = protocolTypes.get(scheme);

            try
            {
                return type.newInstance();
            }
            catch (Exception e)
            {
                // Do nothing; fall through to return null.

                e.printStackTrace();
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Gets an array containing the URI schemes of all the protocol handlers
     * that are currently registered.
     *
     * @return an array containing the URI schemes currently registered
     */
    public String[] getRegisteredSchemes()
    {
        String[] keys = new String[protocolTypes.size()];
        protocolTypes.keySet().toArray(keys);
        return keys;
    }


    // ----------------------------------------------------------
    /**
     * Gets the Java class that implements the protocol with the specified
     * URI scheme.
     *
     * @param scheme the URI scheme of the protocol to retrieve
     * @return the Java class that implements the protocol, or null if there is
     *     no protocol registered with that scheme
     */
    public Class<? extends IProtocol> getProtocolClass(String scheme)
    {
        return protocolTypes.get(scheme);
    }


    //~ Static/instance variables .............................................

    /* The single global instance of the protocol registry. */
    private static ProtocolRegistry instance;

    /* The dictionary that maps URI schemes to protocol handler types. */
    private Map<String, Class<? extends IProtocol>> protocolTypes;
}
