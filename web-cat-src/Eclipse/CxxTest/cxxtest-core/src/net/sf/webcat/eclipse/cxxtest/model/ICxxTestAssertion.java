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
 * Represents an assertion or an assertion-like message generated by the
 * CxxTest runner.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public interface ICxxTestAssertion extends ICxxTestBase
{
	/**
	 * Gets the line number on which the assertion occurred.
	 * 
	 * @return the line number on which the assertion occurred
	 */
	int getLineNumber();

	/**
	 * Gets a string describing the assertion that occurred.
	 * 
	 * @param includeLine true to include the line number in the message;
	 *     otherwise, false.
	 *     
	 * @return a String describing the assertion
	 */
	String getMessage(boolean includeLine);
	
	/**
	 * Gets a stack trace at the point of the assertion, if
	 * available.
	 * 
	 * @return an array of IStackTraceEntry objects describing the
	 * stack trace.
	 */
	ICxxTestStackFrame[] getStackTrace();
}
