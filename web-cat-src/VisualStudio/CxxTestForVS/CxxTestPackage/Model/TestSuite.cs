/*==========================================================================*\
 |  $Id: TestSuite.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using Microsoft.VisualStudio.VCCodeModel;
using WebCAT.CxxTest.VisualStudio.Utility;
using EnvDTE80;
using EnvDTE;

namespace WebCAT.CxxTest.VisualStudio.Model
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Represents a test suite class.
	/// </summary>
	class TestSuite
	{
		//~ Constructors .....................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new test suite based on the specified class in the VC
		/// code model.
		/// </summary>
		/// <param name="testClass">
		/// The VC code model class object from which to create the test
		/// suite.
		/// </param>
		public TestSuite(VCCodeClass testClass)
		{
			this.testClass = (VCCodeElement)testClass;

			Initialize();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Creates a new test suite based on the specified struct in the VC
		/// code model.
		/// </summary>
		/// <param name="testClass">
		/// The VC code model struct object from which to create the test
		/// suite.
		/// </param>
		public TestSuite(VCCodeStruct testClass)
		{
			this.testClass = (VCCodeElement)testClass;

			Initialize();
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of the test suite class.
		/// </summary>
		public string Name
		{
			get
			{
				return testClass.Name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the full path to the source file that contains this test
		/// suite.
		/// </summary>
		public string FullPath
		{
			get
			{
				return testClass.ProjectItem.get_FileNames(1);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the line number in the source code at which this test suite
		/// class starts.
		/// </summary>
		public int LineNumber
		{
			get
			{
				return testClass.StartPoint.Line;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of the C++ object that will be generated to
		/// represent this test suite.
		/// </summary>
		public string ObjectName
		{
			get
			{
				return "suite_" + Name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of the C++ object that will be generated to
		/// represent this test suite description.
		/// </summary>
		public string DescriptionObjectName
		{
			get
			{
				return "suiteDescription_" + Name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of the C++ object that will be generated to
		/// represent the list of test cases found in this test suite.
		/// </summary>
		public string TestListName
		{
			get
			{
				return "Tests_" + Name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the array of test cases in this test suite.
		/// </summary>
		public TestCase[] TestCases
		{
			get
			{
				return testCases.ToArray();
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the line number that contains a createSuite static
		/// method for this test suite, or 0 if there is none.
		/// </summary>
		public int CreateLineNumber
		{
			get
			{
				return createLineNumber;
			}
			set
			{
				createLineNumber = value;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the line number that contains a destroySuite static
		/// method for this test suite, or 0 if there is none.
		/// </summary>
		public int DestroyLineNumber
		{
			get
			{
				return destroyLineNumber;
			}
			set
			{
				destroyLineNumber = value;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether the test suite is dynamic or not
		/// (that is, it contains a createSuite static method).
		/// </summary>
		public bool IsDynamic
		{
			get
			{
				return (createLineNumber != 0);
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Adds a test case method to this test suite.
		/// </summary>
		/// <param name="testCase">
		/// The test case to add to this test suite.
		/// </param>
		public void AddTestCase(TestCase testCase)
		{
			testCases.Add(testCase);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Initializes this test suite with default values.
		/// </summary>
		private void Initialize()
		{
			testCases = new List<TestCase>();

			createLineNumber = 0;
			destroyLineNumber = 0;
		}


		//~ Instance variables ...............................................

		// The VC code model class or struct object for this test suite.
		private VCCodeElement testClass;

		// The list of test case methods contained in this test suite.
		private List<TestCase> testCases;

		// The line number of the createSuite method, or 0 if there is none.
		private int createLineNumber;

		// The line number of the destroySuite method, or 0 if there is none.
		private int destroyLineNumber;
	}
}
