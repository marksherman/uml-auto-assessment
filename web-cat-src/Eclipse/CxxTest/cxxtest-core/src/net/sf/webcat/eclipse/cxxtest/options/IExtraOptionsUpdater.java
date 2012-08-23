/*==========================================================================*\
 |  $Id: IExtraOptionsUpdater.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.options;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.resources.IProject;

/**
 * Maintains the table of extra options handlers defined by all plug-ins
 * currently loaded in Eclipse and provides operations that add and remove
 * options from a project based on version number.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public interface IExtraOptionsUpdater
{
	// ------------------------------------------------------------------------
	/**
	 * Updates the compiler options in a project by invoking the removeOptions
	 * method on old versions of the option handlers, if necessary, then calling
	 * addOptions to set the latest versions of the options in the project.
	 * 
	 * @param project
	 *            the IProject resource that represents the managed C++ project
	 *            to which the options should be added
	 */
	void updateOptions(IProject project);


	// ------------------------------------------------------------------------
	/**
	 * Determines whether a project's compiler options need to be updated. A
	 * project is determined to need an update if:
	 * 
	 * 1) the version of an option set currently used by the project is less
	 * than the highest version of that set loaded by a plug-in 2) an option set
	 * is loaded by a plug-in that does not exist in the project
	 * 
	 * @param project
	 *            the IProject resource whose settings should be checked
	 * 
	 * @return true if the settings need to be updated; otherwise, false.
	 */
	boolean isUpdateNeeded(IProject project);


	// ------------------------------------------------------------------------
	/**
	 * Removes all the extra compiler options that have been injected into a
	 * project through the loaded extension points.
	 * 
	 * @param project
	 *            the IProject resource whose settings should be removed
	 */
	void removeAllOptions(IProject project);
	
	
	// ------------------------------------------------------------------------
	String[] getLatestCxxTestRunnerIncludes(IConfiguration configuration);
}
