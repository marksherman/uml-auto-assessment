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
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestAssertion;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestStackFrame;

import org.xml.sax.Attributes;

/**
 * Creates an appropriate object of type ICxxTestAssertion based on the tag
 * type and attributes from the XML results file.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestAssertionFactory
{
	private static class Assertion implements ICxxTestAssertion
	{
		private CxxTestMethod parent;
		
		private int status;
		
		private String message;
		
		private String[] args;
		
		private int lineNumber;

		public Assertion(CxxTestMethod parent, int lineNumber, int status,
				String message, String[] args)
		{
			this.parent = parent;
			this.status = status;
			this.message = message;
			this.args = args;
			this.lineNumber = lineNumber;

			parent.addAssertion(this);
		}

		public String getMessage(boolean includeLine)
		{
			String[] realArgs = args.clone();

			if(includeLine)
			{
				realArgs[0] = MessageFormat.format(
						Messages.CxxTestAssertionFactory_LineNumber,
						realArgs[0]);
			}
			else
			{
				realArgs[0] = ""; //$NON-NLS-1$
			}

			return MessageFormat.format(message, (Object[]) realArgs);
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
			return null;
		}
	}

	private static final String MSG_TRACE =
		Messages.CxxTestAssertionFactory_TraceMsg;

	private static final String MSG_FAILED_ASSERT =
		Messages.CxxTestAssertionFactory_FailedAssertMsg;

	private static final String MSG_FAILED_ASSERT_EQ =
		Messages.CxxTestAssertionFactory_FailedAssertEq;

	private static final String MSG_FAILED_ASSERT_SAME_DATA =
		Messages.CxxTestAssertionFactory_FailedAssertSameData;

	private static final String MSG_FAILED_ASSERT_DELTA =
		Messages.CxxTestAssertionFactory_FailedAssertDelta;

	private static final String MSG_FAILED_ASSERT_NE =
		Messages.CxxTestAssertionFactory_FailedAssertNe;

	private static final String MSG_FAILED_ASSERT_LT =
		Messages.CxxTestAssertionFactory_FailedAssertLt;

	private static final String MSG_FAILED_ASSERT_LE = 
		Messages.CxxTestAssertionFactory_FailedAssertLe;

	private static final String MSG_FAILED_ASSERT_RELATION =
		Messages.CxxTestAssertionFactory_FailedAssertRelation;

	private static final String MSG_FAILED_ASSERT_PREDICATE =
		Messages.CxxTestAssertionFactory_FailedAssertPredicate;

	private static final String MSG_FAILED_ASSERT_THROWS = 
		Messages.CxxTestAssertionFactory_FailedAssertThrows;

	private static final String MSG_FAILED_ASSERT_NOTHROW =
		Messages.CxxTestAssertionFactory_FailedAssertNoThrow;

	static public ICxxTestAssertion create(CxxTestMethod parent, String type,
			Attributes attributes)
	{
		if(ICxxTestXml.TAG_TRACE.equals(type))
		{
			return createTrace(parent, attributes);
		}
		else if(ICxxTestXml.TAG_WARNING.equals(type))
		{
			return createWarning(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_TEST.equals(type))
		{
			return createFailedTest(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT.equals(type))
		{
			return createFailedAssert(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_EQ.equals(type))
		{
			return createFailedAssertEq(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_SAME_DATA.equals(type))
		{
			return createFailedAssertSameData(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_DELTA.equals(type))
		{
			return createFailedAssertDelta(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_NE.equals(type))
		{
			return createFailedAssertNe(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_LT.equals(type))
		{
			return createFailedAssertLt(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_LE.equals(type))
		{
			return createFailedAssertLe(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_RELATION.equals(type))
		{
			return createFailedAssertRelation(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_PREDICATE.equals(type))
		{
			return createFailedAssertPredicate(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_THROWS.equals(type))
		{
			return createFailedAssertThrows(parent, attributes);
		}
		else if(ICxxTestXml.TAG_FAILED_ASSERT_NOTHROW.equals(type))
		{
			return createFailedAssertNoThrow(parent, attributes);
		}
		else
		{
			return null;
		}
	}

	private static String[] getAttributeValues(Attributes attributes,
			String... attrNames)
	{
		String[] values = new String[attrNames.length];
		
		for(int i = 0; i < attrNames.length; i++)
		{
			values[i] = attributes.getValue(attrNames[i]);
		}
		
		return values;
	}

	private static int getLineNumber(Attributes attributes)
	{
		String value = attributes.getValue(ICxxTestXml.ATTR_LINE);
		return Integer.parseInt(value);
	}

	private static ICxxTestAssertion createTrace(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE, ICxxTestXml.ATTR_MESSAGE);
		int line = getLineNumber(node);
		return new Assertion(parent, line, ICxxTestBase.STATUS_OK,
				MSG_TRACE, values);
	}

	private static ICxxTestAssertion createWarning(
			CxxTestMethod parent, Attributes node)
	{
		int line = getLineNumber(node);
		return new StackTraceAssertion(parent, line,
				ICxxTestBase.STATUS_WARNING);
	}

	private static ICxxTestAssertion createFailedTest(
			CxxTestMethod parent, Attributes node)
	{
		int line = getLineNumber(node);
		return new StackTraceAssertion(parent, line,
				ICxxTestBase.STATUS_ERROR);
	}

	private static ICxxTestAssertion createFailedAssert(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE, ICxxTestXml.ATTR_EXPRESSION);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT, values);
	}

	private static ICxxTestAssertion createFailedAssertEq(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_EQ, values);
	}

	private static ICxxTestAssertion createFailedAssertSameData(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE,
				ICxxTestXml.ATTR_SIZE_DESC, ICxxTestXml.ATTR_SIZE_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_SAME_DATA,
				values);
	}

	private static ICxxTestAssertion createFailedAssertDelta(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE,
				ICxxTestXml.ATTR_DELTA_DESC, ICxxTestXml.ATTR_DELTA_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_DELTA, values);
	}

	private static ICxxTestAssertion createFailedAssertNe(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_NE, values);
	}

	private static ICxxTestAssertion createFailedAssertLt(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_LT, values);
	}

	private static ICxxTestAssertion createFailedAssertLe(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_LE, values);
	}

	private static ICxxTestAssertion createFailedAssertRelation(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_LHS_DESC, ICxxTestXml.ATTR_RHS_DESC,
				ICxxTestXml.ATTR_LHS_VALUE, ICxxTestXml.ATTR_RHS_VALUE,
				ICxxTestXml.ATTR_RELATION);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_RELATION,
				values);
	}

	private static ICxxTestAssertion createFailedAssertPredicate(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE,
				ICxxTestXml.ATTR_ARG_DESC, ICxxTestXml.ATTR_ARG_VALUE,
				ICxxTestXml.ATTR_PREDICATE);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_PREDICATE,
				values);
	}

	private static ICxxTestAssertion createFailedAssertThrows(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE, ICxxTestXml.ATTR_EXPRESSION,
				ICxxTestXml.ATTR_TYPE, ICxxTestXml.ATTR_THREW);
		
		int line = getLineNumber(node);

		if(values[3].equals(
				Messages.CxxTestAssertionFactory_AssertThrowsOtherValue))
		{
			values[3] =
				Messages.CxxTestAssertionFactory_AssertThrewDifferentTypeMsg;
		}
		else
		{
			values[3] =
				Messages.CxxTestAssertionFactory_AssertThrewNothingMsg;
		}

		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_THROWS, values);
	}

	private static ICxxTestAssertion createFailedAssertNoThrow(
			CxxTestMethod parent, Attributes node)
	{
		String[] values = getAttributeValues(node,
				ICxxTestXml.ATTR_LINE, ICxxTestXml.ATTR_EXPRESSION);
		int line = getLineNumber(node);
		return new Assertion(parent, line,
				ICxxTestBase.STATUS_FAILED, MSG_FAILED_ASSERT_NOTHROW, values);
	}
}
