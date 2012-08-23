/*==========================================================================*\
 |  $Id$
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

import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.debug.core.ILaunch;

//------------------------------------------------------------------------
/**
 * This interface can be implemented by clients in order to listen for test
 * run events.  (An extension point for this is not yet implemented.)
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public interface ICxxTestRunListener
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	/**
	 * Called when the test run is about to begin.
	 * 
	 * @param project the project
	 * @param launch the launch configuration
	 */
	void testRunStarted(ICProject project, ILaunch launch);

	
	// ----------------------------------------------------------
	/**
	 * Called when the test run has ended.
	 */
	void testRunEnded();
}
