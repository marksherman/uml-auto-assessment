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

package net.sf.webcat.eclipse.cxxtest.model;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public interface ICxxTestStackFrame
{
	/**
	 * Gets the name and signature of the function for this stack
	 * trace entry.
	 * 
	 * @return A String containing the name and signature of the
	 * function.
	 */
	String getFunction();
	
	/**
	 * Returns the name of the source file in which the function is
	 * located.
	 * 
	 * @return A String containing the name of the source file, or null
	 * if this information was not available.
	 */
	String getFile();
	
	/**
	 * Returns the line number of the function in the source file where
	 * it is located.
	 * 
	 * @return An integer representing the line number of the function,
	 * or 0 if this information was not available.
	 */
	int getLineNumber();
}
