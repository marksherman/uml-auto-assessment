/*==========================================================================*\
 |  $Id: PackagerRegistry.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

import org.webcat.submitter.internal.packagers.JarPackager;
import org.webcat.submitter.internal.packagers.ZipPackager;

//--------------------------------------------------------------------------
/**
 * Manages the packagers that are registered for use by the submitter.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class PackagerRegistry
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes the packager registry and registers the packagers that are
     * built-in to the submitter.
     */
    private PackagerRegistry()
    {
        packagerTypes = new Hashtable<String, Class<? extends IPackager>>();

        // Old IDs for compatibility with the original Java/Eclipse version
        // of the submitter.

        add("net.sf.webcat.eclipse.submitter.packagers.zip", ZipPackager.class);
        add("net.sf.webcat.submitter.packagers.zip", ZipPackager.class);

        // Preferred new ID.

        add("org.webcat.submitter.packagers.zip", ZipPackager.class);

        // Likewise for JARs.

        add("net.sf.webcat.eclipse.submitter.packagers.jar", JarPackager.class);
        add("net.sf.webcat.submitter.packagers.jar", JarPackager.class);
        add("org.webcat.submitter.packagers.jar", JarPackager.class);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the global instance of the PackagerRegistry.
     *
     * @return the global instance of the PackagerRegistry
     */
    public static PackagerRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new PackagerRegistry();
        }

        return instance;
    }


    // ----------------------------------------------------------
    /**
     * Adds a packager to the registry so that it can be referenced in the
     * submission definitions.
     *
     * @param id the identifier of the packager. To guarantee uniqueness, we
     *     recommend using Java-style package naming conventions (that is,
     *     reverse domain name, followed by an appropriate suffix)
     * @param type the type of the packager to be associated with this
     *     identifier, which must implement the {@link IPackager} interface
     */
    public void add(String id, Class<? extends IPackager> type)
    {
        packagerTypes.put(id, type);
    }


    // ----------------------------------------------------------
    /**
     * Called by the submitter internally to create a new instance of the
     * packager with the specified identifier.
     *
     * @param id the identifier of the packager to create
     * @return an instance of the requested packager, or null if no packager
     *     with this identifier was registered
     */
    /*package*/ IPackager createPackagerInstance(String id)
    {
        if (packagerTypes.containsKey(id))
        {
            Class<? extends IPackager> type = packagerTypes.get(id);

            try
            {
                return type.newInstance();
            }
            catch (Exception e)
            {
                // Do nothing, fall through to return null.

                e.printStackTrace();
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Gets an array containing the identifiers of all the packagers that are
     * currently registered.
     *
     * @return an array of Strings containing the identifiers of all the
     *     packagers that are currently registered
     */
    public String[] getRegisteredPackagerIds()
    {
        String[] keys = new String[packagerTypes.size()];
        packagerTypes.keySet().toArray(keys);
        return keys;
    }


    // ----------------------------------------------------------
    /**
     * Gets the Java class that implements the packager with the specified
     * identifier.
     *
     * @param id the unique identifier of the packager to retrieve
     * @return the Java class that implements the packager, or null if there is
     *     no packager registered with that identifier
     */
    public Class<? extends IPackager> getPackagerClass(String id)
    {
        return packagerTypes.get(id);
    }


    //~ Static/instance variables .............................................

    /* The single global instance of the packager registry. */
    private static PackagerRegistry instance;

    /* The dictionary that maps identifiers to packager types. */
    private Map<String, Class<? extends IPackager>> packagerTypes;
}
