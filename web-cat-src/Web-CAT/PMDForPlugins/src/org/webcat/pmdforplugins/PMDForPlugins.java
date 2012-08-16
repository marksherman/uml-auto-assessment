/*==========================================================================*\
 |  $Id: PMDForPlugins.java,v 1.2 2010/09/27 00:46:58 stedwar2 Exp $
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

package org.webcat.pmdforplugins;

import org.webcat.core.Subsystem;
import com.webobjects.foundation.*;

//-------------------------------------------------------------------------
/**
 *  This subsystem provides <a href="http://pmd.sf.net/">PMD</a> for
 *  grading plug-ins.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/27 00:46:58 $
 */
public class PMDForPlugins
    extends Subsystem
{
   //~ Constructors ..........................................................

   // ----------------------------------------------------------
   /**
    * Creates a new PMDForPlugins subsystem object.
    */
   public PMDForPlugins()
   {
       super();
   }


   //~ Methods ...............................................................

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
       // pmd.lib
       addFileBinding(
           properties,
           PMD_LIB_KEY,
           SUBSYSTEM_PREFIX + PMD_LIB_KEY,
           "pmd/lib");
   }


   //~ Instance/static variables .............................................

   private static final String SUBSYSTEM_PREFIX   = "PMDForPlugins.";
   private static final String PMD_LIB_KEY        = "pmd.lib";
}
