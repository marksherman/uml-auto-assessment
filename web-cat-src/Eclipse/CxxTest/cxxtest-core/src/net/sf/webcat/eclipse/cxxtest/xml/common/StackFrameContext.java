/*==========================================================================*\
 |  $Id: StackFrameContext.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.xml.common;

import net.sf.webcat.eclipse.cxxtest.internal.model.CxxTestStackFrame;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class StackFrameContext extends ElementContext
{
	public StackFrameContext(IStackFrameConsumer frameConsumer, Attributes attributes)
	{
		String function = attributes.getValue(ATTR_FUNCTION); //$NON-NLS-1$
		String file = null;
		int lineNumber = 0;
		
		String fileLine = attributes.getValue(ATTR_LOCATION); //$NON-NLS-1$
		if(fileLine != null)
		{
			int colonPos = fileLine.lastIndexOf(':');
			
			if(colonPos != -1)
			{
				file = fileLine.substring(0, colonPos);

				try
				{
					lineNumber = Integer.parseInt(fileLine.substring(colonPos + 1));
				}
				catch(NumberFormatException e)
				{
					file = fileLine;
					lineNumber = 0;
				}
			}
			else
			{
				file = fileLine;
			}
		}
			
		CxxTestStackFrame frame = new CxxTestStackFrame(function, file, lineNumber);
		frameConsumer.addStackFrame(frame);
	}
	
	
	private static final String ATTR_FUNCTION = "function"; //$NON-NLS-1$
	private static final String ATTR_LOCATION = "location"; //$NON-NLS-1$
}
