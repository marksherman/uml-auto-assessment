/*==========================================================================*\
 |  $Id: FeatureProvider.java,v 1.9 2011/04/25 19:08:23 stedwar2 Exp $
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

package net.sf.webcat;

import java.io.*;
import java.net.URL;
import java.util.*;

// -------------------------------------------------------------------------
/**
 *  Represents the provider of a subsystem, including the provider's web
 *  site for obtaining dynamic updates.
 *
 *  @author  stedwar2
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.9 $, $Date: 2011/04/25 19:08:23 $
 */
public class FeatureProvider
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new subsystem provider. This constructor is private, since
     * other classes should use the factory method
     * {@link #getProvider(String)}.
     * @param url the URL identifying this provider, which should point to
     *     an update.properties file
     */
    private FeatureProvider( URL url, InputStream stream )
    {
        this.url = url;
        subsystems = new HashMap<String, FeatureDescriptor>();
        renamedSubsystems = new HashMap<String, String>();
        plugins = new HashMap<String, FeatureDescriptor>();
        renamedPlugins = new HashMap<String, String>();
        loadProperties( stream );
    }


    // ----------------------------------------------------------
    /**
     * Get the subsystem provider associated with the specified URL.
     * @param url the URL for the provider
     * @return the subsystem provider, or null if none exists
     * @throws IOException 
     */
    public static FeatureProvider getProvider( String url ) throws IOException 
    {
        FeatureProvider provider = null;
        URL realURL = baseURLFor( url );
        if ( realURL != null )
        {
            provider = providerRegistry.get( realURL );
            if ( provider == null )
            {
            	InputStream stream =
            		new URL( realURL, UPDATE_PROPERTIES_FILE ).openStream();
                    
            	provider = new FeatureProvider( realURL, stream );
                stream.close();
          
                if ( provider != null )
                {
                    providerRegistry.put( realURL, provider );
                }
            }
        }
        return provider;
    }


    // ----------------------------------------------------------
    /**
     * Get the collection of all registered providers.
     * @return the collection of providers
     */
    public static Collection<FeatureProvider> providers()
    {
        return providerRegistry.values();
    }
    
    // ----------------------------------------------------------
    /**
     * Gets the current version of a feature provider and clears the old one from
     * the registry. 
     */
    public static void refreshProviderRegistry()
    {
    	for(URL realURL : providerRegistry.keySet())
    	{
    		if ( realURL != null )
            {
    			FeatureProvider provider = null;
    			
                provider = providerRegistry.get( realURL );
                if ( provider == null )
                {
					try 
					{
						InputStream stream = new URL( realURL, UPDATE_PROPERTIES_FILE ).openStream();
						provider = new FeatureProvider( realURL, stream );
	                    stream.close();
	              
					} 
					catch (IOException e) 
					{
						provider = null;
					}
                                 	
                    if ( provider != null )
                    {
                        providerRegistry.put( realURL, provider );
                    }
                }
            }
    	}
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Get this provider's name, e.g. "Virginia Tech Computer Science".
     * If the provider does not define a name in its update.properties file,
     * then the URL will be used as the provider's name instead.
     * @return the provider's name as a string
     */
    public String name()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Get this provider's base URL.
     * @return the base URL
     */
    public URL url()
    {
        return url;
    }


    // ----------------------------------------------------------
    /**
     * Get the descriptor for a feature provided by this provider.
     * @param featureName the name of the subsystem or plug-in
     * @return the provider's descriptor for the given feature, or null
     *     if there is none
     */
    public FeatureDescriptor descriptor( String featureName )
    {
        FeatureDescriptor result = subsystemDescriptor( featureName );
        if ( result == null )
        {
            result = pluginDescriptor( featureName );
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Get the descriptor for a subsystem provided by this provider.
     * @param subsystemName the name of the subsystem
     * @return the provider's descriptor for the given subsystem, or null
     *     if there is none
     */
    public FeatureDescriptor subsystemDescriptor( String subsystemName )
    {
        String newName = renamedSubsystems.get( subsystemName );
        if ( newName != null )
        {
            subsystemName = newName;
        }
        return subsystems.get( subsystemName );
    }


    // ----------------------------------------------------------
    /**
     * Get the descriptor for a subsystem provided by this provider.
     * @param pluginName the name of the subsystem
     * @return the provider's descriptor for the given subsystem, or null
     *     if there is none
     */
    public FeatureDescriptor pluginDescriptor( String pluginName )
    {
        String newName = renamedPlugins.get( pluginName );
        if ( newName != null )
        {
            pluginName = newName;
        }
        return plugins.get( pluginName );
    }


    // ----------------------------------------------------------
    /**
     * Get the collection of subsystems provided by this provider.
     * @return the provided subsystems as a collection of descriptors
     */
    public Collection<FeatureDescriptor> subsystems()
    {
        return subsystems.values();
    }


    // ----------------------------------------------------------
    /**
     * Get the collection of plugins provided by this provider.
     * @return the provided plugins as a collection of descriptors
     */
    public Collection<FeatureDescriptor> plugins()
    {
        return plugins.values();
    }


    // ----------------------------------------------------------
    /**
     * Reload this provider's update.properties file and refresh the
     * set of features it provides.
     */
    public void refresh()
    {
        subsystems = new HashMap<String, FeatureDescriptor>();
        plugins = new HashMap<String, FeatureDescriptor>();
        try
        {
            InputStream stream =
                new URL( url, UPDATE_PROPERTIES_FILE ).openStream();
            loadProperties( stream );
            stream.close();
        }
        catch ( IOException e )
        {
            WCUpdater.logError(getClass(), "exception reading from provider at " + url, e );
        }
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Attempt to load the properties settings for this provider.
     */
    private void loadProperties( InputStream stream )
    {
        properties = new Properties();
        try
        {
            properties.load( stream );
        }
        catch ( IOException e )
        {
        	WCUpdater.logError(getClass(), "exception reading from provider at " + url, e );
        }
        name = properties.getProperty( "provider.name", url.toString() );
        for ( Enumeration<?> e = properties.keys(); e.hasMoreElements(); )
        {
            String key = (String)e.nextElement();
            if ( key.startsWith( FeatureDescriptor.SUBSYSTEM_NAME_PREFIX )
                 && key.indexOf( '.',
                   FeatureDescriptor.SUBSYSTEM_NAME_PREFIX.length() ) == -1 )
            {
                String originalName = key.substring(
                    FeatureDescriptor.SUBSYSTEM_NAME_PREFIX.length() );
                String subsystemName = originalName;
                String newName = subsystemName;
                while ( newName != null )
                {
                    subsystemName = newName;
                    newName = properties.getProperty(
                        subsystemName + FeatureDescriptor.RENAME_SUFFIX );
                }
                if ( !originalName.equals( subsystemName ) )
                {
                    renamedSubsystems.put( originalName, subsystemName );
                }
                else if ( subsystems.get( subsystemName ) == null )
                {
                    subsystems.put(
                        subsystemName,
                        new FeatureDescriptor(
                            subsystemName, properties, false ) );
                }
            }
            else if ( key.startsWith( FeatureDescriptor.PLUGIN_NAME_PREFIX )
                      && key.indexOf( '.',
                       FeatureDescriptor.PLUGIN_NAME_PREFIX.length() ) == -1 )
            {
                String originalName = key.substring(
                    FeatureDescriptor.PLUGIN_NAME_PREFIX.length() );
                String pluginName = originalName;
                String newName = pluginName;
                while ( newName != null )
                {
                    pluginName = newName;
                    newName = properties.getProperty(
                        pluginName + FeatureDescriptor.RENAME_SUFFIX );
                }
                if ( !originalName.equals( pluginName ) )
                {
                    renamedPlugins.put( originalName, pluginName );
                }
                else if ( plugins.get( pluginName ) == null )
                {
                    plugins.put(
                        pluginName,
                        new FeatureDescriptor(
                            pluginName, properties, true ) );
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the "canonical" base URL for a given provider, given a
     * publicly listed URL.  The canonical base URL is the URL to the
     * directory containing the update.properties file.
     * @return the canonical URL to the provider's base directory
     */
    private static URL baseURLFor( String url )
    {
        URL result = null;
        String updateUrl = url;
        if ( updateUrl != null )
        {
            if ( updateUrl.endsWith( UPDATE_PROPERTIES_FILE ) )
            {
                updateUrl = updateUrl.substring(
                    0, updateUrl.length() - UPDATE_PROPERTIES_FILE.length() );
            }
            else
            {
                if ( !updateUrl.endsWith( "/" ) )
                {
                    updateUrl = updateUrl + "/";
                }
            }
            try
            {
                result = new URL( updateUrl );
            }
            catch (java.net.MalformedURLException e)
            {
            	WCUpdater.logInfo("FeatureProvider: ERROR: Malformed "
                    + "feature provider URL: " + updateUrl );
            }
        }
        return result;
    }

    //~ Instance/static variables .............................................

    private URL url;
    private String name;
    private Properties properties;
    private Map<String, FeatureDescriptor> subsystems;
    private Map<String, String> renamedSubsystems;
    private Map<String, FeatureDescriptor> plugins;
    private Map<String, String> renamedPlugins;
    private static Map<URL, FeatureProvider> providerRegistry =
        new HashMap<URL, FeatureProvider>();
    private static final String UPDATE_PROPERTIES_FILE = "update.properties";
}
