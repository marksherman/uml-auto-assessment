/*==========================================================================*\
 |  $Id: TestCase.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using EnvDTE80;

namespace WebCAT.CxxTest.VisualStudio.Model
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Represents a test case method.
	/// </summary>
	class TestCase
	{
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new test case from the specified code model function.
		/// </summary>
		/// <param name="function">
		/// The VC code model function object from which to create the test
		/// case.
		/// </param>
		public TestCase(VCCodeFunction function)
		{
			this.function = function;
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of the test case method.
		/// </summary>
		public string Name
		{
			get
			{
				return function.Name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the line number at which the test case method appears in
		/// the source.
		/// </summary>
		public int LineNumber
		{
			get
			{
				return function.StartPoint.Line;
			}
		}


		//~ Instance variables ...............................................

		// The VC code model function object for this test case.
		private VCCodeFunction function;
	}
}
