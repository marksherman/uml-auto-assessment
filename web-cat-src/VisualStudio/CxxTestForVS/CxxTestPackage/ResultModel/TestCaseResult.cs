/*==========================================================================*\
 |  $Id: TestCaseResult.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
	class TestCaseResult : ITestSuiteChildResult
	{
		public TestCaseResult(TestSuiteResult suite,
			XmlAttributeCollection attributes)
		{
			this.suite = suite;
			assertions = new List<ITestAssertion>();
			
			name = attributes["name"].Value;
			line = int.Parse(attributes["line"].Value);

			suite.AddChild(this);
		}

		public ITestResult Parent
		{
			get
			{
				return suite;
			}
		}

		public string Name
		{
			get
			{
				return name;
			}
		}
		
		public int LineNumber
		{
			get
			{
				return line;
			}
		}

		public ITestAssertion[] Assertions
		{
			get
			{
				return assertions.ToArray();
			}
		}
		
		internal void AddAssertion(ITestAssertion assertion)
		{
			assertions.Add(assertion);
		}
		
		public TestResultStatus Status
		{
			get
			{
				TestResultStatus maxStatus = TestResultStatus.OK;

				foreach (ITestAssertion assertion in assertions)
					if (assertion.Status > maxStatus)
						maxStatus = assertion.Status;

				return maxStatus;
			}
		}
		
		public override string ToString()
		{
			return Name;
		}

		private TestSuiteResult suite;

		private List<ITestAssertion> assertions;
		
		private string name;

		private int line;
	}
}
