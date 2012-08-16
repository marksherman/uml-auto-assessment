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
 * Represents a CxxTest test suite class.
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public interface ICxxTestSuite extends ICxxTestBase
{
	/**
	 * Gets the name of the test suite class.
	 * 
	 * @return a String containing the name of the test suite class.
	 */
	String getName();

	/**
	 * Gets the name of the file that contains this test suite.
	 * 
	 * @return a String containing the name of the file.
	 */
	String getFile();

	/**
	 * Gets the line number at which this test suite class starts.
	 * 
	 * @return the line number at which the test suite starts.
	 */
	int getLineNumber();

	/**
	 * Gets an array of the test methods contained in this suite.
	 * 
	 * @return an array of ICxxTestMethod objects.
	 */
	ICxxTestSuiteChild[] getChildren(boolean onlyTests);
}
