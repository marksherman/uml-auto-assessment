/*==========================================================================*\
 |  $Id: LeakContext.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.xml.memstats;

import net.sf.webcat.eclipse.cxxtest.internal.model.DerefereeLeak;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;
import net.sf.webcat.eclipse.cxxtest.xml.common.IStackFrameConsumer;
import net.sf.webcat.eclipse.cxxtest.xml.common.StackFrameContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class LeakContext extends ElementContext implements
	IStackFrameConsumer
{
	private DerefereeLeak leak;

	public LeakContext(DerefereeContext memStats, Attributes attributes)
	{
		leak = new DerefereeLeak(attributes);
		memStats.addLeak(leak);
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_STACK_FRAME.equals(localName))
			return new StackFrameContext(this, attributes);
		else
			return null;
	}

	public void addStackFrame(ICxxTestStackFrame frame)
	{
		leak.addStackFrame(frame);
	}


	private static final String TAG_STACK_FRAME = "stack-frame"; //$NON-NLS-1$
}
