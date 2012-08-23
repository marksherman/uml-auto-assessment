/*==========================================================================*\
 |  $Id: DerefereeLeak.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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

import java.text.MessageFormat;
import java.util.Vector;

import org.xml.sax.Attributes;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeLeak;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class DerefereeLeak implements IDerefereeLeak
{
	public DerefereeLeak(Attributes attributes)
	{
		address = attributes.getValue(ATTR_ADDRESS);
		size = Integer.parseInt(attributes.getValue(ATTR_SIZE));
		
		if("yes".equals(attributes.getValue(ATTR_ARRAY))) //$NON-NLS-1$
		{
			array = true;
		}
		else
		{
			array = false;
		}
		
		stackTrace = new Vector<ICxxTestStackFrame>();
	}

	public void addStackFrame(ICxxTestStackFrame frame)
	{
		stackTrace.add(frame);
	}

	public String getAddress()
	{
		return address;
	}

	public int getSize()
	{
		return size;
	}

	public boolean isArray()
	{
		return array;
	}

	public ICxxTestStackFrame[] getStackTrace()
	{
		ICxxTestStackFrame[] frames =
			new ICxxTestStackFrame[stackTrace.size()];
		
		stackTrace.toArray(frames);
		return frames;
	}
	
	public String toString()
	{
		if (isArray())
		{
			return MessageFormat.format(
					Messages.DerefereeLeak_ArrayDescription,
					getAddress(), getSize());
		}
		else
		{
			return MessageFormat.format(
					Messages.DerefereeLeak_BlockDescription,
					getAddress(), getSize());
		}
	}

	private static final String ATTR_ADDRESS = "address"; //$NON-NLS-1$
	
	private static final String ATTR_SIZE = "size"; //$NON-NLS-1$

	private static final String ATTR_ARRAY = "array"; //$NON-NLS-1$

	private String address;
	
	private int size;
	
	private boolean array;

	private Vector<ICxxTestStackFrame> stackTrace;
}
