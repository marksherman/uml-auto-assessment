/*==========================================================================*\
 |  $Id: TestSuiteCollector.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using EnvDTE;
using WebCAT.CxxTest.VisualStudio.Model;
using System.Text.RegularExpressions;
using Microsoft.VisualStudio.VCCodeModel;
using System.IO;
using Microsoft.VisualStudio;
using WebCAT.CxxTest.VisualStudio.Utility;

namespace WebCAT.CxxTest.VisualStudio.Model
{
	class TestSuiteCollector : CodeElementVisitor
	{
		public override bool Visit(ProjectItem projectItem)
		{
			if (new Guid(projectItem.Kind) == VSConstants.GUID_ItemType_PhysicalFile)
			{
				if (projectItem.Name == Constants.TestRunnerFilename)
				{
					return false;
				}
				else
				{
					if (ContainsCxxTestIncludeDirective(projectItem))
					{
						possibleTestFiles.Add(
							new PossibleTestFile(projectItem), true);
					}

					return true;
				}
			}
			else
			{
				return true;
			}
		}

		public override bool Visit(CodeElement element)
		{
			bool visitChildren;

			switch (element.Kind)
			{
				case vsCMElement.vsCMElementNamespace:
					visitChildren = true;
					currentSuite = null;
					break;

				case vsCMElement.vsCMElementClass:
					/*
					 * Only visit a class's or struct's children if it inherits
					 * from the CxxTest suite class.
					 */
					visitChildren = IsClassTestSuite((VCCodeClass)element);

					if (visitChildren)
					{
						TestCaseCollector collector = new TestCaseCollector();
						collector.Process(element.Children);

						currentSuite.CreateLineNumber = collector.CreateLineNumber;
						currentSuite.DestroyLineNumber = collector.DestroyLineNumber;

						foreach (TestCase testCase in collector.TestCases)
							currentSuite.AddTestCase(testCase);

						visitChildren = false;
					}

					break;

				case vsCMElement.vsCMElementStruct:
					/*
					 * Only visit a class's or struct's children if it inherits
					 * from the CxxTest suite class.
					 */
					visitChildren = IsStructTestSuite((VCCodeStruct)element);

					if (visitChildren)
					{
						TestCaseCollector collector = new TestCaseCollector();
						collector.Process(element.Children);

						currentSuite.CreateLineNumber = collector.CreateLineNumber;
						currentSuite.DestroyLineNumber = collector.DestroyLineNumber;

						foreach (TestCase testCase in collector.TestCases)
							currentSuite.AddTestCase(testCase);

						visitChildren = false;
					}

					break;

				case vsCMElement.vsCMElementFunction:
					visitChildren = false;

					if (currentSuite == null)
					{
						/*
						 * Check global functions to see if the user has created a
						 * main() function. If so, we need to generate the test
						 * runner as a static object instead.
						 */
						CheckForMain((VCCodeFunction)element);
					}

					break;

				default:
					visitChildren = false;
					break;
			}

			return visitChildren;
		}

		public TestSuite[] Suites
		{
			get
			{
				return testSuites.ToArray();
			}
		}

		public PossibleTestFile[] PossibleTestFiles
		{
			get
			{
				PossibleTestFile[] items =
					new PossibleTestFile[possibleTestFiles.Count];
				possibleTestFiles.Keys.CopyTo(items, 0);
				return items;
			}
		}

		public bool MainFunctionExists
		{
			get
			{
				return mainExists;
			}
		}

		private bool ContainsCxxTestIncludeDirective(ProjectItem projectItem)
		{
			string contents = File.ReadAllText(projectItem.get_FileNames(1));

			MatchCollection matches =
				Constants.CxxTestHeaderRegex.Matches(contents);

			if (matches.Count > 0)
				return true;
			else
				return false;
		}

		/**
		 * Determines if a class is a valid CxxTest test suite by checking
		 * for CxxTest::TestSuite in the superclass list.
		 * 
		 * @param element the class handle to check
		 * 
		 * @return true if the class is a test suite; otherwise, false.
		 */
		private bool IsClassTestSuite(VCCodeClass klass)
		{
			foreach (VCCodeElement element in klass.Bases)
			{
				if (Constants.CxxTestSuiteClassRegex.IsMatch(element.Name))
				{
					currentSuite = new TestSuite(klass);
					testSuites.Add(currentSuite);

					ProjectItem item = klass.ProjectItem;
					if (item != null)
						RemovePossibleTestFile(item);

					return true;
				}
			}

			return false;
		}

		private bool IsStructTestSuite(VCCodeStruct strukt)
		{
			foreach (VCCodeElement element in strukt.Bases)
			{
				if (Constants.CxxTestSuiteClassRegex.IsMatch(element.Name))
				{
					currentSuite = new TestSuite(strukt);
					testSuites.Add(currentSuite);

					ProjectItem item = strukt.ProjectItem;
					if (item != null)
						RemovePossibleTestFile(item);

					return true;
				}
			}

			return false;
		}

		private void RemovePossibleTestFile(ProjectItem item)
		{
			PossibleTestFile toRemove = null;

			foreach (PossibleTestFile file in possibleTestFiles.Keys)
			{
				if (file.FullPath == item.get_FileNames(1))
				{
					toRemove = file;
					break;
				}
			}

			if (toRemove != null)
				possibleTestFiles.Remove(toRemove);
		}

		/**
		 * Checks a global function to determine if it is the main() function.
		 * 
		 * @param element the function handle to be checked.
		 */
		private void CheckForMain(VCCodeFunction function)
		{
			if (function.Name == "main")
				mainExists = true;
		}

		/**
		 * The current suite being processed.
		 */
		private TestSuite currentSuite = null;

		/**
		 * A vector containing all the test suites processed.
		 */
		private List<TestSuite> testSuites = new List<TestSuite>();

		private Dictionary<PossibleTestFile, bool> possibleTestFiles =
			new Dictionary<PossibleTestFile, bool>();

		/**
		 * Keeps track of whether a main() function was encountered during
		 * the traversal.
		 */
		private bool mainExists = false;
	}
}
