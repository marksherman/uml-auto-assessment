/*==========================================================================*\
 |  $Id: NewTestSuiteGenerator.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using Microsoft.VisualStudio.VCCodeModel;
using EnvDTE;
using System.IO;
using Microsoft.VisualStudio.VCProjectEngine;
using Antlr.StringTemplate;
using System.Reflection;
using Antlr.StringTemplate.Language;
using System.Collections;

namespace WebCAT.CxxTest.VisualStudio.Templating
{
	internal class TestSuiteGenerator
	{
		public TestSuiteGenerator(HierarchyItem project,
			string suiteName, HierarchyItem headerUnderTest,
			string superclass, bool createSetUp, bool createTearDown,
			VCCodeFunction[] functions)
		{
			this.project = project;
			this.suiteName = suiteName;
			this.headerUnderTest = headerUnderTest;
			this.superclass = superclass;
			this.createSetUp = createSetUp;
			this.createTearDown = createTearDown;
			this.functions = functions;
			
			GenerateUniqueStubNames();
		}

		public HierarchyItem GeneratedSuiteFile
		{
			get
			{
				return suiteFile;
			}
		}

		public void Generate()
		{
			string projectDir = project.ProjectDirectory;
			string headerName = suiteName + ".h";

			string suitePath = Path.Combine(projectDir, headerName);

			StreamWriter writer = File.CreateText(suitePath);
			WriteHeaderFileContent(writer, suitePath);
			writer.Close();

			VCFilter testFilter = GetOrCreateTestFilter();
			VCProjectItem item = (VCProjectItem)testFilter.AddFile(suitePath);

			suiteFile = project.GetChildWithName(item.ItemName);
		}

		private void GenerateUniqueStubNames()
		{
			int stubCount = functions.Length;

			stubNames = new string[stubCount];
			object[] stubElements = new object[stubCount];

			for (int i = 0; i < stubCount; i++)
			{
				stubNames[i] = functions[i].Name;
				stubElements[i] = functions[i];
			}

			bool anotherPass, fixIthName;

			do
			{
				anotherPass = false;
				fixIthName = false;

				for (int i = 0; i < stubCount; i++)
				{
					for (int j = 0; j < stubCount; j++)
					{
						if (i != j && stubNames[i] == stubNames[j])
						{
							// The names of the stubs are equal, so go up a step and
							// try to further qualify them.

							if (!(stubElements[j] is ProjectItem))
							{
								stubElements[j] = ((VCCodeElement)stubElements[j]).Parent;

								string jPrefix;

								if (!(stubElements[j] is ProjectItem))
									jPrefix = ((VCCodeElement)stubElements[j]).Name;
								else
									jPrefix = "_global";

								stubNames[j] = jPrefix + "_" + stubNames[j];

								anotherPass = true;
								fixIthName = true;
							}
						}
					}

					if (fixIthName)
					{
						if (!(stubElements[i] is ProjectItem))
						{
							stubElements[i] = ((VCCodeElement)stubElements[i]).Parent;

							string iPrefix;
							if (!(stubElements[i] is ProjectItem))
								iPrefix = ((VCCodeElement)stubElements[i]).Name;
							else
								iPrefix = "_global";

							stubNames[i] = iPrefix + "_" + stubNames[i];
						}

						fixIthName = false;
					}
				}
			} while (anotherPass);

			for (int i = 0; i < stubNames.Length; i++)
			{
				stubNames[i] = "test" +
					char.ToUpper(stubNames[i][0]) + stubNames[i].Substring(1);
			}
		}

		private VCFilter GetOrCreateTestFilter()
		{
			VCProject vcproj = (VCProject)project.GetExtObjectAs<Project>().Object;

			IVCCollection filters = (IVCCollection)vcproj.Filters;
			VCFilter filter = (VCFilter)filters.Item("Test Suites");

			if (filter == null)
			{
				filter = (VCFilter)vcproj.AddFilter("Test Suites");
			}

			return filter;
		}
		
		private void WriteHeaderFileContent(TextWriter writer, string suitePath)
		{
			Stream stream =
				Assembly.GetExecutingAssembly().GetManifestResourceStream(
				NewTestSuiteTemplateResourcePath);

			StringTemplateGroup templateGroup = new StringTemplateGroup(
				new StreamReader(stream), typeof(AngleBracketTemplateLexer));

			templateGroup.RegisterAttributeRenderer(typeof(string),
				new NewTestSuiteStringRenderer(suitePath));

			StringTemplate template =
				templateGroup.GetInstanceOf("newSuiteFile");

			// Initialize the options that will be passed into the template.

			Hashtable options = new Hashtable();
			options["suiteName"] = suiteName;
			options["superclass"] = superclass;
			options["headerUnderTest"] = headerUnderTest.CanonicalName;
			options["createSetUp"] = createSetUp;
			options["createTearDown"] = createTearDown;

			template.SetAttribute("options", options);
			template.SetAttribute("testCases", new List<string>(stubNames));

			template.Write(new AutoIndentWriter(writer));
		}

		private const string NewTestSuiteTemplateResourcePath =
			"WebCAT.CxxTest.VisualStudio.Resources.Templates.newsuite.stg";

		private HierarchyItem project;
		private string suiteName;
		private HierarchyItem headerUnderTest;
		private string superclass;
		private bool createSetUp;
		private bool createTearDown;
		private VCCodeFunction[] functions;
		private string[] stubNames;

		private HierarchyItem suiteFile;
	}
}
