/*==========================================================================*\
 |  $Id: DocumentContext.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

import java.util.Vector;

import net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuite;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class DocumentContext extends ElementContext
{
	private Vector<ICxxTestSuite> suites;
	
	public DocumentContext()
	{
		suites = new Vector<ICxxTestSuite>();
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_WORLD.equals(localName))
			return new WorldContext(this, attributes);
		else
			return null;
	}

	public void addSuite(ICxxTestSuite suite)
	{
		suites.add(suite);
	}

	public ICxxTestSuite[] getSuites()
	{
		return suites.toArray(new ICxxTestSuite[suites.size()]);
	}
	
	
	private static final String TAG_WORLD = "world"; //$NON-NLS-1$
}
