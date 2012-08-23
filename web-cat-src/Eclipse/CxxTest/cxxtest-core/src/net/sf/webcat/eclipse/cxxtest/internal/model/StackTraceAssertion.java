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
import java.util.Vector;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestAssertion;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class StackTraceAssertion implements ICxxTestAssertion
{
	public StackTraceAssertion(CxxTestMethod parent, int lineNumber, int status)
	{
		this.parent = parent;
		this.status = status;
		this.lineNumber = lineNumber;

		parent.addAssertion(this);
		
		stackTrace = new Vector<ICxxTestStackFrame>();
	}

	public String getMessage(boolean includeLine)
	{
		String[] realArgs = new String[2];
		realArgs[0] = Integer.toString(lineNumber);
		realArgs[1] = message;

		if(includeLine && lineNumber > 0)
		{
			realArgs[0] = MessageFormat.format(Messages.StackTraceAssertion_LineNumber, realArgs[0]);
		}
		else
		{
			realArgs[0] = ""; //$NON-NLS-1$
		}
		
		if(status == ICxxTestBase.STATUS_WARNING)
		{
			return MessageFormat.format(MSG_WARNING, (Object[]) realArgs);
		}
		else
		{
			return MessageFormat.format(MSG_FAILED_TEST, (Object[]) realArgs);
		}
	}

	public void setMessage(String msg)
	{
		message = msg;
	}

	public void addStackFrame(ICxxTestStackFrame frame)
	{
		stackTrace.add(frame);
	}

	public ICxxTestBase getParent()
	{
		return parent;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public int getStatus()
	{
		return status;
	}
	
	public ICxxTestStackFrame[] getStackTrace()
	{
		ICxxTestStackFrame[] frames =
			new ICxxTestStackFrame[stackTrace.size()];

		stackTrace.toArray(frames);
		return frames;
	}


	private static final String MSG_WARNING =
		Messages.StackTraceAssertion_WarningMsg;

	private static final String MSG_FAILED_TEST =
		Messages.StackTraceAssertion_FailureMsg;

	private CxxTestMethod parent;
	
	private int status;
	
	private String message;
	
	private Vector<ICxxTestStackFrame> stackTrace;
	
	private int lineNumber;
}
