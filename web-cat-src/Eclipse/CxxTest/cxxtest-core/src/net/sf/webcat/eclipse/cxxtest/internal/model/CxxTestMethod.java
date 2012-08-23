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

package net.sf.webcat.eclipse.cxxtest.internal.model;

import java.util.Vector;

import net.sf.webcat.eclipse.cxxtest.model.ICxxTestAssertion;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestMethod;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuite;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestMethod implements ICxxTestMethod
{
	private ICxxTestSuite suite;

	private Vector<ICxxTestAssertion> assertions;
	
	private String name;

	private int line;

	public CxxTestMethod(CxxTestSuite suite, Attributes attributes)
	{
		this.suite = suite;
		assertions = new Vector<ICxxTestAssertion>();
		
		suite.addChild(this);

		name = attributes.getValue(ATTR_NAME);
		String lineStr = attributes.getValue(ATTR_LINE);
		
		line = Integer.parseInt(lineStr);
	}

	public ICxxTestBase getParent()
	{
		return suite;
	}

	public String getName()
	{
		return name;
	}
	
	public int getLineNumber()
	{
		return line;
	}

	public ICxxTestAssertion[] getFailedAssertions()
	{
		return assertions.toArray(new ICxxTestAssertion[assertions.size()]);
	}
	
	protected void addAssertion(ICxxTestAssertion assertion)
	{
		assertions.add(assertion);
	}
	
	public int getStatus()
	{
		int maxStatus = STATUS_OK;

		for(int i = 0; i < assertions.size(); i++)
		{
			ICxxTestAssertion test = assertions.get(i);
			if(test.getStatus() > maxStatus)
				maxStatus = test.getStatus();
		}
		
		return maxStatus;
	}
	
	public String toString()
	{
		return getName();
	}

	private static final String ATTR_NAME = "name"; //$NON-NLS-1$
	private static final String ATTR_LINE = "line"; //$NON-NLS-1$
}
