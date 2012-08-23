/*==========================================================================*\
 |  $Id: ICxxTestSuiteChild.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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
 * This interface represents a type of message that can occur as the direct
 * child of a CxxTest test suite. Currently this is used to represent test
 * methods and test suite initialization errors.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public interface ICxxTestSuiteChild extends ICxxTestBase
{
	/**
	 * Gets the line number at which this element starts.
	 * 
	 * @return the line number at which this element starts.
	 */
	public int getLineNumber();
}
