/*==========================================================================*\
 |  $Id: PerlForPlugins.java,v 1.2 2010/09/27 00:46:00 stedwar2 Exp $
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

package org.webcat.perlforplugins;

import com.webobjects.foundation.*;
import java.io.File;
import org.apache.log4j.Logger;
import org.webcat.core.Subsystem;

// -------------------------------------------------------------------------
/**
 *  This subsystem provides Perl modules for use by grading plug-ins
 *  implemented in Perl.  It provides the following packages:
 *  <ul>
 *  <li><p>Web_CAT::*, for generating HTML printouts from source files,
 *      post-processing Clover-produced HTML, and merging diagnostic
 *      messages into HTML printouts.</p>
 *  <li><p>XML::Smart and its requirements, for reading and writing XML
 *      documents.</p>
 *  <li><p>Config::Properties::Simple and its requirements, for reading
 *      and writing properties files.</p>
 *  </ul>
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/27 00:46:00 $
 */
public class PerlForPlugins
    extends Subsystem
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new PerlForPlugins subsystem object.
     */
    public PerlForPlugins()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Add any subsystem-specific command-line environment variable bindings
     * to the given dictionary.
     * @param env the dictionary to add environment variable bindings to;
     * the full set of currently available bindings are passed in.
     */
    public void addEnvironmentBindings(NSMutableDictionary<String, String> env)
    {
        String lib = myResourcesDir() + "/lib";
        File libDir = new File(lib);
        if (libDir.exists())
        {
            try
            {
                String path = libDir.getCanonicalPath();
                Object valueObj = env.valueForKey(PERLLIB_KEY);
                if (valueObj != null)
                {
                    path = path + System.getProperty("path.separator")
                        + valueObj.toString();
                }
                env.takeValueForKey(path, PERLLIB_KEY);
            }
            catch (java.io.IOException e)
            {
                log.error("Attempting to get canonical path for " + lib, e);
            }
        }
        else
        {
            log.error("Cannot locate PERLLIB in Resources directory");
        }
    }


    // ----------------------------------------------------------
    /**
     * Add any subsystem-specific plug-in property bindings
     * to the given dictionary.
     * @param properties the dictionary to add new properties to;
     * individual plug-in information may override these later.
     */
    public void addPluginPropertyBindings(
        NSMutableDictionary<String, String> properties)
    {
        properties.takeValueForKey(
            System.getProperty(FILE_SEPARATOR_KEY),
            SUBSYSTEM_PREFIX + FILE_SEPARATOR_KEY);
        properties.takeValueForKey(
            System.getProperty(PATH_SEPARATOR_KEY),
            SUBSYSTEM_PREFIX + PATH_SEPARATOR_KEY);
        properties.takeValueForKey(
            System.getProperty(LINE_SEPARATOR_KEY),
            SUBSYSTEM_PREFIX + LINE_SEPARATOR_KEY);
    }


    //~ Instance/static variables .............................................

    private static final String SUBSYSTEM_PREFIX = "PerlForPlugins.";
    private static final String PERLLIB_KEY = "PERLLIB";
    private static final String FILE_SEPARATOR_KEY = "file.separator";
    private static final String PATH_SEPARATOR_KEY = "path.separator";
    private static final String LINE_SEPARATOR_KEY = "line.separator";
    static Logger log = Logger.getLogger(PerlForPlugins.class);
}
