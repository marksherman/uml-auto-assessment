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

import java.text.MessageFormat;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestStackFrame implements ICxxTestStackFrame
{
	private String function;
	
	private String file;
	
	private int lineNumber;

	public CxxTestStackFrame(String function, String file, int lineNumber)
	{
		this.function = function;
		this.file = file;
		this.lineNumber = lineNumber;
	}

	public String getFunction()
	{
		return function;
	}

	public String getFile()
	{
		return file;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}
	
	public String toString()
	{
		String str = function;
		
		if(file != null)
		{
			if (lineNumber != 0)
			{
				str = MessageFormat.format(
						Messages.CxxTestStackFrame_FileAndLineNumber,
						file, lineNumber);
			}
			else
			{
				str = MessageFormat.format(
						Messages.CxxTestStackFrame_FileOnly, file);
			}
		}
		
		return str;
	}
}