/*==========================================================================*\
 |  $Id: SuiteErrorContext.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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
import net.sf.webcat.eclipse.cxxtest.internal.model.CxxTestSuiteError;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;
import net.sf.webcat.eclipse.cxxtest.xml.common.IStackFrameConsumer;
import net.sf.webcat.eclipse.cxxtest.xml.common.StackFrameContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class SuiteErrorContext extends ElementContext
	implements IStackFrameConsumer
{
	private CxxTestSuiteError suiteError;

	private StringBuffer contents;

	public SuiteErrorContext(CxxTestSuite suite, Attributes attributes)
	{
		contents = new StringBuffer();
		
		suiteError = new CxxTestSuiteError(suite, attributes);
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_STACK_FRAME.equals(localName))
			return new StackFrameContext(this, attributes);
		
		return null;
	}

	public void endElement(String uri, String localName, String qName)
	{
		suiteError.setMessage(contents.toString().trim());		
	}
	
	public void characters(char[] chars, int start, int length)
	{
		for(int i = start; i < start + length; i++)
			if(chars[i] != '\r' && chars[i] != '\n')
				contents.append(chars[i]);
	}

	public void addStackFrame(ICxxTestStackFrame frame)
	{
		suiteError.addStackFrame(frame);
	}
	

	private static final String TAG_STACK_FRAME = "stack-frame"; //$NON-NLS-1$
}
