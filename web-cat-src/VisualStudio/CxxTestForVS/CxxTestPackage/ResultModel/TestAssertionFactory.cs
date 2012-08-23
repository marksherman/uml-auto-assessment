/*==========================================================================*\
 |  $Id: TestAssertionFactory.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest integration package for Visual
 |	Studio.NET.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class TestAssertionFactory
	{
		private class Assertion : ITestAssertion
		{
			public Assertion(TestCaseResult parent, int lineNumber,
				TestResultStatus status, string message, string[] args)
			{
				this.parent = parent;
				this.status = status;
				this.message = message;
				this.args = args;
				this.lineNumber = lineNumber;

				parent.AddAssertion(this);
			}

			public string GetMessage(bool includeLine)
			{
				string[] realArgs = (string[]) args.Clone();

				if(includeLine)
					realArgs[0] = " (line " + realArgs[0] + ")";
				else
					realArgs[0] = "";

				return string.Format(message, realArgs);
			}

			public ITestResult Parent
			{
				get
				{
					return parent;	
				}
			}

			public int LineNumber
			{
				get
				{
					return lineNumber;
				}
			}

			public TestResultStatus Status
			{
				get
				{
					return status;
				}
			}
			
			public BacktraceFrame[] Backtrace
			{
				get
				{
					return null;
				}
			}

			private TestCaseResult parent;
			
			private TestResultStatus status;
			
			private string message;
			
			private string[] args;
			
			private int lineNumber;
		}

		static public ITestAssertion Create(TestCaseResult parent,
			string type, XmlAttributeCollection attributes)
		{
			switch (type)
			{
				case "trace":
					return CreateTrace(parent, attributes);

				case "warning":
					return CreateWarning(parent, attributes);

				case "failed-test":
					return CreateFailedTest(parent, attributes);

				case "failed-assert":
					return CreateFailedAssert(parent, attributes);

				case "failed-assert-eq":
					return CreateFailedAssertEq(parent, attributes);

				case "failed-assert-same-data":
					return CreateFailedAssertSameData(parent, attributes);

				case "failed-assert-delta":
					return CreateFailedAssertDelta(parent, attributes);

				case "failed-assert-ne":
					return CreateFailedAssertNe(parent, attributes);

				case "failed-assert-lt":
					return CreateFailedAssertLt(parent, attributes);

				case "failed-assert-le":
					return CreateFailedAssertLe(parent, attributes);

				case "failed-assert-relation":
					return CreateFailedAssertRelation(parent, attributes);

				case "failed-assert-predicate":
					return CreateFailedAssertPredicate(parent, attributes);

				case "failed-assert-throws":
					return CreateFailedAssertThrows(parent, attributes);

				case "failed-assert-nothrow":
					return CreateFailedAssertNoThrow(parent, attributes);

				default:
					return null;
			}
		}

		private static string[] GetAttributeValues(
			XmlAttributeCollection attributes, string[] attrNames)
		{
			string[] values = new string[attrNames.Length];
			
			for(int i = 0; i < attrNames.Length; i++)
				values[i] = attributes[attrNames[i]].Value;
			
			return values;
		}

		private static int GetLineNumber(XmlAttributeCollection attributes)
		{
			return int.Parse(attributes["line"].Value);
		}

		private static ITestAssertion CreateTrace(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] { "line", "message" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.OK, MsgTrace, values);
		}

		private static ITestAssertion CreateWarning(TestCaseResult parent, XmlAttributeCollection node)
		{
			int line = GetLineNumber(node);
			return new AssertionWithStackTrace(parent, line, TestResultStatus.Warning);
		}

		private static ITestAssertion CreateFailedTest(TestCaseResult parent, XmlAttributeCollection node)
		{
			int line = GetLineNumber(node);
			return new AssertionWithStackTrace(parent, line, TestResultStatus.Error);
		}

		private static ITestAssertion CreateFailedAssert(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] { "line", "expression" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssert, values);
		}

		private static ITestAssertion CreateFailedAssertEq(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertEq, values);
		}

		private static ITestAssertion CreateFailedAssertSameData(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value", "size-desc", "size-value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertSameData, values);
		}

		private static ITestAssertion CreateFailedAssertDelta(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value", "delta-desc", "delta-value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertDelta, values);
		}

		private static ITestAssertion CreateFailedAssertNe(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertNE, values);
		}

		private static ITestAssertion CreateFailedAssertLt(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertLT, values);
		}

		private static ITestAssertion CreateFailedAssertLe(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertLE, values);
		}

		private static ITestAssertion CreateFailedAssertRelation(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "lhs-desc", "rhs-desc", "lhs-value", "rhs-value", "relation" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertRelation, values);
		}

		private static ITestAssertion CreateFailedAssertPredicate(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "arg-desc", "arg-desc", "predicate" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertPredicate, values);
		}

		private static ITestAssertion CreateFailedAssertThrows(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "expression", "type", "threw" });
			
			int line = GetLineNumber(node);

			if(values[3] == "other")
				values[3] = "it threw a different type";
			else
				values[3] = "it threw nothing";

			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertThrows, values);
		}

		private static ITestAssertion CreateFailedAssertNoThrow(TestCaseResult parent, XmlAttributeCollection node)
		{
			string[] values = GetAttributeValues(node, new string[] {
					"line", "expression" });
			int line = GetLineNumber(node);
			return new Assertion(parent, line, TestResultStatus.Failed, MsgFailedAssertNoThrow, values);
		}
		
		private const string MsgTrace = "Trace{0}: {1}";

		private const string MsgFailedAssert =
			"Failed assertion{0}: expected {1} == true, but found false";

		private const string MsgFailedAssertEq =
			"Failed assertion{0}: expected {1} == {2}, but found {3} != {4}";

		private const string MsgFailedAssertSameData =
			"Failed assertion{0}: expected {5} ({6}) bytes equal at {1} and {2}, but found {3} differs from {4}";

		private const string MsgFailedAssertDelta =
			"Failed assertion{0}: expected {1} == {2} to within {5} ({6}), but found {3} != {4}";

		private const string MsgFailedAssertNE =
			"Failed assertion{0}: expected {1} != {2}, but found both equal {3}";

		private const string MsgFailedAssertLT =
			"Failed assertion{0}: expected {1} < {2}, but found {3} >= {4}";

		private const string MsgFailedAssertLE = 
			"Failed assertion{0}: expected {1} <= {2}, but found {3} > {4}";

		private const string MsgFailedAssertRelation =
			"Failed assertion{0}: expected {5}({1}, {2}) == true, but found {5}({3}, {4}) == false";

		private const string MsgFailedAssertPredicate =
			"Failed assertion{0}: expected {3}({1}) == true, but found {3}({2}) == false";

		private const string MsgFailedAssertThrows = 
			"Failed assertion{0}: expected \"{1}\" to throw \"{2}\", but {3}";

		private const string MsgFailedAssertNoThrow =
			"Failed assertion{0}: expected \"{1}\" not to throw an exception, but it did";

	}
}
