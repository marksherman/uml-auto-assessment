/*==========================================================================*\
 |  $Id: TestCaseCollector.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using WebCAT.CxxTest.VisualStudio.Utility;
using EnvDTE;
using Microsoft.VisualStudio.VCCodeModel;

namespace WebCAT.CxxTest.VisualStudio.Model
{
	class TestCaseCollector : CodeElementVisitor
	{
		public TestCaseCollector()
		{
			testCases = new List<TestCase>();
		}

		public override bool Visit(CodeElement element)
		{
			switch (element.Kind)
			{
				case vsCMElement.vsCMElementFunction:
					CheckMethod((VCCodeFunction)element);
					break;
			}

			return false;
		}

		public TestCase[] TestCases
		{
			get
			{
				return testCases.ToArray();
			}
		}

		public int CreateLineNumber
		{
			get
			{
				return createLineNumber;
			}
		}

		public int DestroyLineNumber
		{
			get
			{
				return destroyLineNumber;
			}
		}

		/**
		 * Checks a method inside a test suite class to determine if it
		 * is a valid test method (void return value, no arguments, name
		 * begins with "test"). If so, it is added to the suite's list of
		 * tests.
		 * 
		 * @param element the method handle to be checked.
		 */
		private void CheckMethod(VCCodeFunction function)
		{
			string name = function.Name;
			int lineNum = function.StartPoint.Line;
			bool isStatic = function.IsShared;

			if (name.StartsWith("Test") || name.StartsWith("test"))
			{
				if (function.Type.AsString == "void" &&
					IsMethodParameterless(function))
				{
					testCases.Add(new TestCase(function));
				}
			}
			else if (name == "createSuite")
			{
				if (isStatic &&
					function.Type.AsString.IndexOf('*') >= 0 &&
					IsMethodParameterless(function))
				{
					createLineNumber = lineNum;
				}
			}
			else if (name == "destroySuite")
			{
				CodeElements parameters = function.Parameters;

				if (parameters.Count == 1)
				{
					VCCodeParameter parameter =
						(VCCodeParameter)parameters.Item(1);

					if (isStatic &&
						parameter.Type.AsString.IndexOf('*') >= 0 &&
						function.Type.AsString == "void")
					{
						destroyLineNumber = lineNum;
					}
				}
			}
		}

		/**
		 * A convenience function to check if a function takes no arguments
		 * (CDT's DOM treats a function with no arguments differently from one
		 * with a single "void" argument).
		 * 
		 * @param element the method handle to check.
		 * 
		 * @return true if the function has no arguments; otherwise, false.
		 */
		private bool IsMethodParameterless(VCCodeFunction function)
		{
			if (function.Parameters.Count == 0)
			{
				return true;
			}
			else if (function.Parameters.Count > 1)
			{
				return false;
			}
			else
			{
				VCCodeParameter parameter =
					(VCCodeParameter)function.Parameters.Item(1);

				return (parameter.Type.AsString == "void");
			}
		}

		private List<TestCase> testCases;
		private int createLineNumber;
		private int destroyLineNumber;
	}
}
