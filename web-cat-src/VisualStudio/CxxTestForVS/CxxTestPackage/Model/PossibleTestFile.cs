/*==========================================================================*\
 |  $Id: PossibleTestFile.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.Model
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Represents a "possible test file" in the project. A possible test file
	/// is a header file that includes cxxtest/TestSuite.h but does not
	/// subclass the CxxTest::TestSuite class.
	/// </summary>
	class PossibleTestFile
	{
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a possible test file based on the specified project item.
		/// </summary>
		/// <param name="item">
		/// The project item representing the header file that is believed to
		/// be a possible test file.
		/// </param>
		public PossibleTestFile(ProjectItem item)
		{
			this.projectItem = item;
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets the full path to the file.
		/// </summary>
		public string FullPath
		{
			get
			{
				return projectItem.get_FileNames(1);
			}
		}


		//~ Instance variables ...............................................

		// The project item representing the header file.
		private ProjectItem projectItem;
	}
}
