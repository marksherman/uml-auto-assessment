/*==========================================================================*\
 |  $Id: TestRunnerGenerator.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using WebCAT.CxxTest.VisualStudio.Model;
using System.IO;
using EnvDTE;
using System.Reflection;
using Microsoft.VisualStudio.VCCodeModel;
using WebCAT.CxxTest.VisualStudio.Utility;
using Antlr.StringTemplate;
using Antlr.StringTemplate.Language;
using System.Collections;
using System.Collections.Specialized;

namespace WebCAT.CxxTest.VisualStudio.Templating
{
	class TestRunnerGenerator
	{
		/**
		 * Instantiates an instance of the CxxTestDriverGenerator for the
		 * specified project and test suite collection.
		 * 
		 * @param project the ICProject associated with this generator
		 * @param path the path of the source file to be generated
		 * @param suites the collection of test suites to be generated
		 * 
		 * @throws IOException if an I/O error occurs during generation
		 */
		public TestRunnerGenerator(string path, TestSuiteCollection suites,
			Dictionary<string, bool> testsToRun)
		{
			this.suites = suites;
			this.runnerPath = path;

			// Create a proxy object to manage the tests to run. Any tests
			// not in this map are assumed to be true (so that if tests
			// have been added, but not refreshed in the tool window, they
			// will be run until they are explicitly disabled).

			this.testsToRunProxy = new TestsToRunProxy(testsToRun);

			// Load the template from the embedded assembly resources.

			Stream stream =
				Assembly.GetExecutingAssembly().GetManifestResourceStream(
				RunnerTemplateResourcePath);

			StringTemplateGroup templateGroup = new StringTemplateGroup(
				new StreamReader(stream), typeof(AngleBracketTemplateLexer));

			templateGroup.RegisterAttributeRenderer(typeof(string),
				new TestRunnerStringRenderer(path));

			template = templateGroup.GetInstanceOf("runAllTestsFile");

			// Initialize the options that will be passed into the template.

			options = new Hashtable();
			options["platformIsMSVC"] = true;
			options["trapSignals"] = true;
			options["traceStack"] = true;
			options["noStaticInit"] = true;
			options["root"] = true;
			options["part"] = false;
			options["abortOnFail"] = true;
			options["longLongType"] = null;
			options["mainProvided"] = suites.MainFunctionExists;
			options["runner"] = "XmlStdioPrinter";
			options["xmlOutput"] = true;
			options["testResultsFilename"] = Constants.TestResultsFilename;
			options["testsToRun"] = testsToRunProxy;

			writer = File.CreateText(path);
		}
		
		public void Generate()
		{
			template.SetAttribute("options", options);
			template.SetAttribute("suites",
				new List<TestSuite>(suites.Suites));

			if (suites.PossibleTestFiles.Length > 0)
			{
				template.SetAttribute("possibleTestFiles",
					suites.PossibleTestFiles);
			}

			template.Write(new AutoIndentWriter(writer));

			writer.Close();
		}


		private class TestsToRunProxy
		{
			public TestsToRunProxy(IDictionary<string, bool> testsToRun)
			{
				this.testsToRun = testsToRun;
			}

			public bool this[string test]
			{
				get
				{
					if (testsToRun == null)
						return false;
					else if (!testsToRun.ContainsKey(test))
						return true;
					else
						return testsToRun[test];
				}
			}

			private IDictionary<string, bool> testsToRun;
		}

		private const string RunnerTemplateResourcePath =
			"WebCAT.CxxTest.VisualStudio.Resources.Templates.runner.stg";

		private string runnerPath;
		private TestSuiteCollection suites;
		private TestsToRunProxy testsToRunProxy;
		private Hashtable options;
		private StringTemplate template;
		private TextWriter writer;
	}
}
