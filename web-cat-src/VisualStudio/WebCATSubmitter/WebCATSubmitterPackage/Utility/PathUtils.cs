/*==========================================================================*\
 |  $Id: PathUtils.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission Package for Visual
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

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// Provides methods to assist in the manipulation of paths.
	/// </summary>
	static class PathUtils
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Given a directory and a full path to another file or directory that
		/// descends from it, this method returns the part of the latter path
		/// that is relative to the former.
		/// </summary>
		/// <param name="directory">
		/// The ancestor directory.
		/// </param>
		/// <param name="fullPath">
		/// The descendant file or folder.
		/// </param>
		/// <returns>
		/// The portion of the descendant path that is relative to the ancestor
		/// path, or null if the descendant is not actually contained in the
		/// ancestor.
		/// </returns>
		public static string GetRelativePath(string directory, string fullPath)
		{
			string absDirectory = Path.GetFullPath(directory);
			string absFullPath = Path.GetFullPath(fullPath);

			if (absFullPath.StartsWith(absDirectory))
			{
				string relPath = absFullPath.Substring(absDirectory.Length);

				if (relPath.Length > 0 &&
					relPath[0] == Path.DirectorySeparatorChar)
				{
					relPath = relPath.Substring(1);
				}

				return relPath;
			}
			else
			{
				return null;
			}
		}
	}
}
