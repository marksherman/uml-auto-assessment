/*==========================================================================*\
 |  $Id: ANTForPlugins.java,v 1.2 2010/09/26 23:39:39 stedwar2 Exp $
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

package org.webcat.antforplugins;

import com.webobjects.foundation.*;
import java.io.File;
import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.Subsystem;

//-------------------------------------------------------------------------
/**
 *  This subsystem provides ANT, Checkstyle, and PMD for grading plug-ins.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:39:39 $
 */
public class ANTForPlugins
   extends Subsystem
{
   //~ Constructors ..........................................................

   // ----------------------------------------------------------
   /**
    * Creates a new PerlForPlugins subsystem object.
    */
   public ANTForPlugins()
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
       // JAVA_HOME
       String userSetting = Application.configurationProperties()
           .getProperty(SUBSYSTEM_PREFIX + JAVA_HOME_KEY);
       if (userSetting != null)
       {
           env.takeValueForKey(userSetting,  JAVA_HOME_KEY);
       }

       // ANT_HOME
       addFileBinding(
           env,
           ANT_HOME_KEY,
           SUBSYSTEM_PREFIX + ANT_HOME_KEY,
           "ant");

       // Add JAVA_HOME/bin to path
       Object javaHomeObj = env.valueForKey(JAVA_HOME_KEY);
       if (javaHomeObj != null)
       {
           String path = javaHomeObj.toString()
               + System.getProperty("file.separator") + "bin";
           File javaBinDir = new File(path);
           if (javaBinDir.exists())
           {
               path = javaBinDir.getAbsolutePath();
               // Handle the fact that Windows variants often use "Path"
               // instead of "PATH"
               String pathKey = PATH_KEY2;
               Object valueObj = env.valueForKey(pathKey);
               if (valueObj == null)
               {
                   pathKey = PATH_KEY1;
                   valueObj = env.valueForKey(pathKey);
               }
               if (valueObj != null)
               {
                   path = path + System.getProperty("path.separator")
                       + valueObj.toString();
               }
               env.takeValueForKey(path, pathKey);
           }
           else
           {
               log.error(
                   "no bin directory found in JAVA_HOME: " + javaHomeObj);
           }
       }


       // Add ANT_HOME/bin to path
       Object antHomeObj = env.valueForKey(ANT_HOME_KEY);
       if (antHomeObj != null)
       {
           String path = antHomeObj.toString()
               + System.getProperty("file.separator") + "bin";
           File antBinDir = new File(path);
           if (antBinDir.exists())
           {
               path = antBinDir.getAbsolutePath();
               // Handle the fact that Windows variants often use "Path"
               // instead of "PATH"
               String pathKey = PATH_KEY2;
               Object valueObj = env.valueForKey(pathKey);
               if (valueObj == null)
               {
                   pathKey = PATH_KEY1;
                   valueObj = env.valueForKey(pathKey);
               }
               if (valueObj != null)
               {
                   path = path + System.getProperty("path.separator")
                       + valueObj.toString();
               }
               env.takeValueForKey(path, pathKey);
           }
           else
           {
               log.error("no bin directory found in ANT_HOME: " + antHomeObj);
           }
       }
   }


   //~ Instance/static variables .............................................

   private static final String SUBSYSTEM_PREFIX   = "ANTForPlugins.";
   private static final String JAVA_HOME_KEY      = "JAVA_HOME";
   private static final String ANT_HOME_KEY       = "ANT_HOME";
   private static final String PATH_KEY1          = "PATH";
   private static final String PATH_KEY2          = "Path";
   static Logger log = Logger.getLogger(ANTForPlugins.class);
}
