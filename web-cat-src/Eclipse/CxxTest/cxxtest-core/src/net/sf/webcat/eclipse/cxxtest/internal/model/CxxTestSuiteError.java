/*==========================================================================*\
 |  $Id: CxxTestSuiteError.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteError;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class CxxTestSuiteError implements ICxxTestSuiteError
{
	private CxxTestSuite suite;

	private String errorType;

	private int line;

	private String msg;
	
	private Vector<ICxxTestStackFrame> stackTrace;

	public CxxTestSuiteError(CxxTestSuite suite, Attributes attributes)
	{
		this.suite = suite;
		stackTrace = new Vector<ICxxTestStackFrame>();

		errorType = attributes.getValue(ATTR_TYPE);
		
		String lineStr = attributes.getValue(ATTR_LINE);
		line = Integer.parseInt(lineStr);

		suite.addChild(this);
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteChild#getLineNumber()
	 */
	public int getLineNumber()
	{
		return line;
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteError#getMessage()
	 */
	public String getMessage()
	{
		return msg;
	}

	public void setMessage(String msg)
	{
		this.msg = msg;
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteError#getName()
	 */
	public String getName()
	{
		if("init".equals(errorType)) //$NON-NLS-1$
			return "<initialization error>"; //$NON-NLS-1$
		else
			return errorType;
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteError#getStackTrace()
	 */
	public ICxxTestStackFrame[] getStackTrace()
	{
		ICxxTestStackFrame[] frames =
			new ICxxTestStackFrame[stackTrace.size()];
		stackTrace.toArray(frames);
		return frames;
	}

	public void addStackFrame(ICxxTestStackFrame frame)
	{
		stackTrace.add(frame);
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase#getParent()
	 */
	public ICxxTestBase getParent()
	{
		return suite;
	}

	/* (non-Javadoc)
	 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase#getStatus()
	 */
	public int getStatus()
	{
		return ICxxTestBase.STATUS_ERROR;
	}

	public String toString()
	{
		return getName();
	}

	private static final String ATTR_TYPE = "type"; //$NON-NLS-1$
	private static final String ATTR_LINE = "line"; //$NON-NLS-1$
}
