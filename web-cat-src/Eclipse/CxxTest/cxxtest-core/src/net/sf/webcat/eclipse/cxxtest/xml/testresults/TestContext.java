/*==========================================================================*\
 |  $Id: TestContext.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.xml.testresults;

import org.xml.sax.Attributes;

import net.sf.webcat.eclipse.cxxtest.internal.model.CxxTestMethod;
import net.sf.webcat.eclipse.cxxtest.internal.model.CxxTestSuite;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class TestContext extends ElementContext 
{
	private CxxTestMethod test;

	public TestContext(CxxTestSuite suite, Attributes attributes)
	{
		test = new CxxTestMethod(suite, attributes);
	}
	
	public ElementContext startElement(String uri, String localName, String qName, Attributes attributes)
	{
		return new AssertionContext(test, localName, attributes);
	}
}
