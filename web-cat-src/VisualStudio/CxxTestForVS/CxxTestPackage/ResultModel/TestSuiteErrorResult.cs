/*==========================================================================*\
 |  $Id: TestSuiteErrorResult.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
	class TestSuiteErrorResult : ITestSuiteChildResult
	{
		public TestSuiteErrorResult(TestSuiteResult suite,
			XmlAttributeCollection attributes)
		{
			this.suite = suite;
			backtrace = new List<BacktraceFrame>();

			errorType = attributes["type"].Value;
			line = int.Parse(attributes["line"].Value);

			suite.AddChild(this);
		}

		/* (non-Javadoc)
		 * @see net.sf.webcat.eclipse.cxxtest.model.ICxxTestSuiteChild#getLineNumber()
		 */
		public int LineNumber
		{
			get
			{
				return line;
			}
		}

		public string Message
		{
			get
			{
				return msg;
			}
			set
			{
				msg = value;
			}
		}

		public string Name
		{
			get
			{
				if (errorType == "init")
					return "<initialization error>";
				else
					return errorType;
			}
		}

		public BacktraceFrame[] Backtrace
		{
			get
			{
				return backtrace.ToArray();
			}
		}

		public void AddBacktraceFrame(BacktraceFrame frame)
		{
			backtrace.Add(frame);
		}

		public ITestResult Parent
		{
			get
			{
				return suite;
			}
		}

		public TestResultStatus Status
		{
			get
			{
				return TestResultStatus.Error;
			}
		}

		public override string ToString()
		{
			return Name;
		}

		private TestSuiteResult suite;

		private string errorType;

		private int line;

		private string msg;
		
		private List<BacktraceFrame> backtrace;
	}
}
