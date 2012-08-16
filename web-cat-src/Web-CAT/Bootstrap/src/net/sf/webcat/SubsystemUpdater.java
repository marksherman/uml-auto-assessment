/*==========================================================================*\
 |  $Id: SubsystemUpdater.java,v 1.6 2011/04/25 19:08:23 stedwar2 Exp $
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
import java.util.Enumeration;
import java.util.Properties;

// -------------------------------------------------------------------------
/**
 *  This class represents a Web-CAT subsystem, and provides the facilities
 *  necessary to check for and update the subsystem to the lastest version
 *  available from its provider.
 *
 *  @author  stedwar2
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.6 $, $Date: 2011/04/25 19:08:23 $
 */
public class SubsystemUpdater
    extends FeatureDescriptor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param root the subdirectory for this subsystem
     */
    public SubsystemUpdater( File root )
    {
        this.root = root;
        this.isPlugin = false;
        loadPropertiesIfPossible();
    }


    //~ Public Constants ......................................................

    public static final String[] JAVA_ARCHIVE_EXTENSIONS = { ".jar", ".zip" };

    //~ Public Methods ........................................................
    
    // ----------------------------------------------------------
    /**
     * Add all of this subsystem's jars to a buffer that will be used to
     * generate a classpath value.
     * @param classpath a bufer in which the classpath is being built; this
     *        is a newline-separated list of jar file or directory names
     */
    public void addToClasspath( StringBuffer classpath )
    {
        String disabled = getProperty( "disabled" );
        if ( disabled != null
            && (   disabled.equals( "1" )
                || disabled.equals( "true" )
                || disabled.equals( "yes" ) ) )
        {
            // Ignore this subsystem if it is disabled
            return;
        }
        File jarDir = new File( root.getAbsolutePath() + JAVA_RESOURCES );
        if ( jarDir.exists() && jarDir.isDirectory() )
        {
            File[] entries = jarDir.listFiles();
            for ( int i = 0; i < entries.length; i++ )
            {
                for ( int j = 0; j < JAVA_ARCHIVE_EXTENSIONS.length; j++ )
                {
                    if ( entries[i].getName().endsWith(
                         JAVA_ARCHIVE_EXTENSIONS[j] ) )
                    {
                        classpath.append( entries[i]
                            .getAbsolutePath().replace( '\\', '/' ) );
                        classpath.append( "\n" );
                        break;
                    }
                }
            }
        }
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Attempt to load the properties settings for this subsystem.
     */
    private void loadPropertiesIfPossible()
    {
        properties = new Properties();
        File propertiesFile =
            new File( root.getAbsolutePath() + PROPERTIES_FILE1 );
        if ( !propertiesFile.exists() )
        {
            propertiesFile =
                new File( root.getAbsolutePath() + PROPERTIES_FILE2 );
        }
        if ( propertiesFile.exists() )
        {
            try
            {
                InputStream is = new FileInputStream( propertiesFile );
                properties.load( is );
                is.close();
            }
            catch ( IOException e )
            {
                // We're not using log4j, since that may be within a
                // subsystem that needs updating
            	WCUpdater.logError( getClass(), "Error loading properties from "
                           + propertiesFile.getAbsolutePath()
                           + ":",
                           e );
            }
        }
        for ( Enumeration<?> e = properties.keys(); e.hasMoreElements(); )
        {
            String key = (String)e.nextElement();
            if ( key.startsWith( SUBSYSTEM_NAME_PREFIX ) )
            {
                name = key.substring( SUBSYSTEM_NAME_PREFIX.length() );
            }
        }
    }

    //~ Instance/static variables .............................................

    private File root;
    // private boolean deleteBeforeUpdate = true;

    private static final String PROPERTIES_FILE1 = "/Resources/Properties";
    private static final String PROPERTIES_FILE2 = "/Properties";
    private static final String JAVA_RESOURCES   = "/Resources/Java";
}
