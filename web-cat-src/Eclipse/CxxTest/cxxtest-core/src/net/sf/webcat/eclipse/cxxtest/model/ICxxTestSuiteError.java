/*==========================================================================*\
 |  $Id: ICxxTestSuiteError.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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
 * Represents a major error that affects an entire test suite during
 * execution. Currently, this is used to represent an error trapped during
 * initialization of a test suite (the constructor for one of its fields
 * raising a signal, for instance), but it could be expanded to other
 * types of errors in the future.
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public interface ICxxTestSuiteError extends ICxxTestSuiteChild
{
	/**
	 * Gets the type of error that occurred.
	 * 
	 * @return a String indicating the type of error that occurred. 
	 */
	String getName();
	
	/**
	 * Gets the message associated with the error.
	 * 
	 * @return a String indicating the error message.
	 */
	String getMessage();
	
	/**
	 * Gets the stack trace that indicates where the error occurred.
	 * 
	 * @return an array of ICxxTestStackFrame objects that represent the
	 *     stack trace.
	 */
	ICxxTestStackFrame[] getStackTrace();
}
