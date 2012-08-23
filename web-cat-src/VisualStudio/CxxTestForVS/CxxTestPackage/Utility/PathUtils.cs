/*==========================================================================*\
 |  $Id: PathUtils.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using System.IO;

namespace WebCAT.CxxTest.VisualStudio.Utility
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Utility functions to ease dealing with file paths.
	/// </summary>
	public class PathUtils
	{
		// ------------------------------------------------------
		/// <summary>
		/// Gets a relative path string, using "." and ".." if necessary, to
		/// show how one would navigate to <code>toPath</code> from the
		/// directory that is or contains <code>fromPath</code>.
		/// </summary>
		/// <param name="fromPath">
		/// The path to navigate from.
		/// </param>
		/// <param name="toPath">
		/// The path to navigate to.
		/// </param>
		/// <returns>
		/// The relative path from <code>fromPath</code> to
		/// <code>toPath</code>, if there exists a relative path between them.
		/// Otherwise, the complete <code>toPath</code> is returned.
		/// </returns>
		public static string RelativePathTo(string fromPath, string toPath)
		{
			StringBuilder result = new StringBuilder(1024);

			uint attrFrom, attrTo;

			if (fromPath.EndsWith("\\"))
				fromPath = fromPath.Substring(0, fromPath.Length - 1);

			if (toPath.EndsWith("\\"))
				toPath = toPath.Substring(0, toPath.Length - 1);

			if (Directory.Exists(fromPath))
				attrFrom = NativeMethods.FILE_ATTRIBUTE_DIRECTORY;
			else
				attrFrom = NativeMethods.FILE_ATTRIBUTE_NORMAL;

			if (Directory.Exists(toPath))
				attrTo = NativeMethods.FILE_ATTRIBUTE_DIRECTORY;
			else
				attrTo = NativeMethods.FILE_ATTRIBUTE_NORMAL;

			bool success = NativeMethods.PathRelativePathTo(
				result, fromPath, attrFrom, toPath, attrTo);

			if (!success)
				return toPath;
			else
			{
				string relPath = result.ToString();

				if (relPath.StartsWith(".\\"))
					relPath = relPath.Substring(2);

				return relPath;
			}
		}
	}
}
