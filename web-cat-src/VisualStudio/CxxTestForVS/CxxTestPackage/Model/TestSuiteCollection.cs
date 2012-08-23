/*==========================================================================*\
 |  $Id: TestSuiteCollection.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using Microsoft.VisualStudio.Shell.Interop;
using EnvDTE;
using Microsoft.VisualStudio.VCCodeModel;
using WebCAT.CxxTest.VisualStudio.Utility;
using WebCAT.CxxTest.VisualStudio.Templating;

namespace WebCAT.CxxTest.VisualStudio.Model
{
	internal class TestSuiteCollection
	{
		public TestSuiteCollection(IVsSolution solution)
		{
			this.solution = solution;
			this.suites = null;
		}

		public IVsSolution Solution
		{
			get
			{
				return solution;
			}
		}

		public TestSuite[] Suites
		{
			get
			{
				return suites;
			}
		}

		public PossibleTestFile[] PossibleTestFiles
		{
			get
			{
				return possibleTestFiles;
			}
		}

		public bool MainFunctionExists
		{
			get
			{
				return mainExists;
			}
		}

		public void PopulateFromSolution()
		{
			TestSuiteCollector collector = new TestSuiteCollector();

			foreach (HierarchyItem projectHier in
				VsShellUtils.GetLoadedProjects(solution))
			{
				Project project = projectHier.GetExtObjectAs<Project>();
				collector.Process(project);
			}

			suites = collector.Suites;
			possibleTestFiles = collector.PossibleTestFiles;
			mainExists = collector.MainFunctionExists;
		}

		private IVsSolution solution;

		private TestSuite[] suites;

		private PossibleTestFile[] possibleTestFiles;

		private bool mainExists;
	}
}
