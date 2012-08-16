/*==========================================================================*\
 |  $Id: Constants.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
using System.Text.RegularExpressions;

namespace WebCAT.CxxTest.VisualStudio
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Various constants used by the CxxTest package.
	/// </summary>
	public static class Constants
	{
		//~ Static variables .................................................

		/// <summary>
		/// A regular expression that matches the name of the CxxTest suite
		/// base class in the superclass list.  This will match either
		/// TestSuite, CxxTest::TestSuite, or ::CxxTest::TestSuite.
		/// </summary>
		public static readonly Regex CxxTestSuiteClassRegex =
			new Regex(@"((::)?\s*CxxTest\s*::\s*)?TestSuite");


		/// <summary>
		/// A regular expression that matches the line that includes the
		/// CxxTest test suite header file.
		/// </summary>
		public static readonly Regex CxxTestHeaderRegex =
			new Regex(@"\s*#\s*include\s+<cxxtest/TestSuite.h>");


		/// <summary>
		/// The name of the automatically generated test runner source file.
		/// </summary>
		public const string TestRunnerFilename = "_cxxtest_runAllTests.cpp";


		/// <summary>
		/// The name of the test results log file that is generated when the
		/// tests are executed.
		/// </summary>
		public const string TestResultsFilename = "_testResults.log";


		/// <summary>
		/// The name of the Dereferee log file that contains memory leak and
		/// usage statistics.
		/// </summary>
		public const string MemoryResultsFilename = "_dereferee.log";


		/// <summary>
		/// The name of the obfuscated binary log file used to collect student
		/// usage statistics (not yet used).
		/// </summary>
		public const string BinaryLogFilename = "_testResults.dat";
	}
}
