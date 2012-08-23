/*==========================================================================*\
 |  $Id: SuiteContext.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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

import net.sf.webcat.eclipse.cxxtest.internal.model.CxxTestSuite;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class SuiteContext extends ElementContext
{
	private CxxTestSuite suite;

	public SuiteContext(WorldContext world, Attributes attributes)
	{
		suite = new CxxTestSuite(attributes);
		world.addSuite(suite);
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_SUITE_ERROR.equals(localName))
			return new SuiteErrorContext(suite, attributes);
		else if(TAG_TEST.equals(localName))
			return new TestContext(suite, attributes);
		else
			return null;
	}
	

	private static final String TAG_SUITE_ERROR = "suite-error"; //$NON-NLS-1$

	private static final String TAG_TEST = "test"; //$NON-NLS-1$
}
