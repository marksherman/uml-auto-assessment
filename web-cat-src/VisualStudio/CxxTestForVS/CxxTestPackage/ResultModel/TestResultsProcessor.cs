/*==========================================================================*\
 |  $Id: TestResultsProcessor.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using System.IO;

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class TestResultsProcessor
	{
		public TestResultsProcessor(string resultsPath)
		{
			suiteResults = new List<TestSuiteResult>();

			try
			{
				using (TextReader textReader = File.OpenText(resultsPath))
				using (XmlReader reader = XmlReader.Create(textReader))
				{
					XmlDocument document = new XmlDocument();
					document.Load(reader);

					ProcessDocument(document);
				}
			}
			catch (Exception e)
			{
				// Handle this.
			}
		}

		public TestSuiteResult[] SuiteResults
		{
			get
			{
				return suiteResults.ToArray();
			}
		}

		private void ProcessDocument(XmlNode documentNode)
		{
			foreach (XmlNode child in documentNode.ChildNodes)
			{
				if (child.Name == "world")
					ProcessWorld(child);
			}
		}

		private void ProcessWorld(XmlNode worldNode)
		{
			foreach (XmlNode child in worldNode.ChildNodes)
			{
				if (child.Name == "suite")
					ProcessSuite(child);
			}
		}

		private void ProcessSuite(XmlNode suiteNode)
		{
			TestSuiteResult suite = new TestSuiteResult(suiteNode.Attributes);
			suiteResults.Add(suite);

			foreach (XmlNode child in suiteNode.ChildNodes)
			{
				if (child.Name == "suite-error")
					ProcessSuiteError(suite, child);
				else if (child.Name == "test")
					ProcessTest(suite, child);
			}
		}

		private void ProcessSuiteError(TestSuiteResult suite, XmlNode errorNode)
		{
			TestSuiteErrorResult error = new TestSuiteErrorResult(suite, errorNode.Attributes);
			error.Message = errorNode.InnerText;

			foreach (XmlNode child in errorNode.ChildNodes)
			{
				if(child.Name == "stack-frame")
				{
					BacktraceFrame frame = ProcessStackFrame(child);
					error.AddBacktraceFrame(frame);
				}
			}
		}

		private BacktraceFrame ProcessStackFrame(XmlNode frameNode)
		{
			string function = frameNode.Attributes["function"].Value;
			string file = null;
			int lineNumber = 0;

			if (frameNode.Attributes["location"] != null)
			{
				string fileLine = frameNode.Attributes["location"].Value;
				if (fileLine != null)
				{
					int colonPos = fileLine.LastIndexOf(':');

					if (colonPos != -1)
					{
						file = fileLine.Substring(0, colonPos);

						if (!int.TryParse(fileLine.Substring(colonPos + 1),
							out lineNumber))
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
			}

			return new BacktraceFrame(function, file, lineNumber);
		}

		private void ProcessTest(TestSuiteResult suite, XmlNode testNode)
		{
			TestCaseResult testCase = new TestCaseResult(suite, testNode.Attributes);

			foreach (XmlNode child in testNode.ChildNodes)
			{
				ITestAssertion assertion = TestAssertionFactory.Create(
					testCase, child.Name, child.Attributes);

				if(assertion is AssertionWithStackTrace)
				{
					AssertionWithStackTrace ast = (AssertionWithStackTrace)assertion;

					ast.SetMessage(child.InnerText);

					foreach (XmlNode child2 in child.ChildNodes)
					{
						if (child2.Name == "stack-frame")
						{
							BacktraceFrame frame = ProcessStackFrame(child2);
							ast.AddBacktraceFrame(frame);
						}
					}
				}
			}
		}

		private List<TestSuiteResult> suiteResults;
	}
}
