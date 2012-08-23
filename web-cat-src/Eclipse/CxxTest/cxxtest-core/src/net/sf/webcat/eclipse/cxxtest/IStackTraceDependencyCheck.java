/*==========================================================================*\
 |  $Id: IStackTraceDependencyCheck.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest;

import org.eclipse.core.runtime.IProgressMonitor;

//------------------------------------------------------------------------
/**
 * This interface is implemented by extensions of the
 * net.sf.webcat.eclipse.cxxtest.stackTraceDependencyCheck extension point to
 * look for required dependencies that must be installed before stack tracing
 * can be enabled in CxxTest. 
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public interface IStackTraceDependencyCheck
{
	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Performs the dependency check defined by the implementor.
	 * 
	 * @param monitor the progress monitor to use to update the state of the
	 *     task
	 * @return true if the dependencies were found; otherwise, false
	 */
	boolean checkForDependencies(IProgressMonitor monitor);

	
	// ----------------------------------------------------------
	/**
	 * Gets a string that lists the dependencies that could not be found, if
	 * any.
	 * 
	 * @return a string that lists the dependencies that could not be found
	 */
	String missingDependencies();
}
