/*==========================================================================*\
 |  $Id: AssertionWithStackTrace.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class AssertionWithStackTrace : ITestAssertion
	{
		public AssertionWithStackTrace(TestCaseResult parent,
			int lineNumber, TestResultStatus status)
		{
			this.parent = parent;
			this.status = status;
			this.lineNumber = lineNumber;

			parent.AddAssertion(this);
			
			backtrace = new List<BacktraceFrame>();
		}

		public string GetMessage(bool includeLine)
		{
			string[] realArgs = new string[2];
			realArgs[0] = lineNumber.ToString();
			realArgs[1] = message;

			if(includeLine && lineNumber > 0)
				realArgs[0] = " (line " + realArgs[0] + ")";
			else
				realArgs[0] = "";
			
			if(status == TestResultStatus.Warning)
				return string.Format(MsgWarning, realArgs);
			else
				return string.Format(MsgFailedTest, realArgs);
		}

		public void SetMessage(string msg)
		{
			message = msg;
		}

		public void AddBacktraceFrame(BacktraceFrame frame)
		{
			backtrace.Add(frame);
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
				return backtrace.ToArray();
			}
		}

		private const string MsgWarning = "Warning{0}: {1}";
		private const string MsgFailedTest = "Failed test{0}: {1}";

		private TestCaseResult parent;
		
		private TestResultStatus status;
		
		private string message;
		
		private List<BacktraceFrame> backtrace;
		
		private int lineNumber;
	}
}
