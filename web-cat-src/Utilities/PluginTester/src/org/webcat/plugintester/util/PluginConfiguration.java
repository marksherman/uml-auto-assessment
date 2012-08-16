/*==========================================================================*\
 |  $Id: PluginConfiguration.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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

package org.webcat.plugintester.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.plist.PropertyListConfiguration;

//-------------------------------------------------------------------------
/**
 * Provides access to various properties of a plugin's config.plist
 * description.
 * 
 * @author Tony Allevato
 * @version $Id: PluginConfiguration.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
 */
public class PluginConfiguration
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the PluginConfiguration class for the plugin
     * in the specified directory.
     * 
     * @param pluginDir the directory in which the plugin resides
     */
    public PluginConfiguration(File pluginDir) throws ConfigurationException
    {
        File configFile = new File(pluginDir, "config.plist");
        config = new PropertyListConfiguration(configFile);
    }

    
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the value of a property located at the root of the plugin
     * configuration.
     * 
     * @param key the name of the property
     * @return the string value of the property
     */
    public String getRootProperty(String key)
    {
        return config.getProperty(key).toString();
    }


    // ----------------------------------------------------------
    /**
     * Gets a map containing all of the options (both per-assignment and
     * reusable) defined by the grading plugin.
     * 
     * @return a Map containing the plugin options; the keys are the names of
     *     the options, and the values are PropertyListConfiguration objects
     *     that describe the properties for those options
     */
    public Map<String, PropertyListConfiguration> getOptions()
    {
        Map<String, PropertyListConfiguration> options =
            new TreeMap<String, PropertyListConfiguration>();

        List<?> optionPlists = config.getList("options");
        addOptionsToMap(optionPlists, options);

        optionPlists = config.getList("assignmentOptions");
        addOptionsToMap(optionPlists, options);

        optionPlists = config.getList("globalOptions");
        addOptionsToMap(optionPlists, options);

        return options;
    }

    
    // ----------------------------------------------------------
    /**
     * Adds the options from the specified list to a map keyed on the names of
     * those options.
     * 
     * @param plists the list containing the options
     * @param map the map into which the options will be copied
     */
    private void addOptionsToMap(List<?> plists,
            Map<String, PropertyListConfiguration> map)
    {
        if (plists != null)
        {
            for (Object optionObj : plists)
            {
                if (optionObj instanceof PropertyListConfiguration)
                {
                    PropertyListConfiguration optionPlist =
                        (PropertyListConfiguration) optionObj;

                    String property = optionPlist.getString("property");
                    map.put(property, optionPlist);
                }
            }
        }
    }


    //~ Instance/static variables .............................................

    private PropertyListConfiguration config;
}
