/*==========================================================================*\
 |  $Id: SubmittableDirectFile.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using WebCAT.Submitter;
using WebCAT.Submitter.VisualStudio.Utility;
using System.IO;

namespace WebCAT.Submitter.VisualStudio.Submittables
{
	/// <summary>
	/// A submittable item that represents a file on the file system referred
	/// to by its absolute path name.
	/// </summary>
	/// <remarks>
	/// This class is used to handle "special" files that are yielded by a
	/// solution or project item, such as the .sln file or
	/// .csproj/.vcproj/etc. files.
	/// </remarks>
	class SubmittableDirectFile : ISubmittableItem
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable direct file item.
		/// </summary>
		/// <param name="rootPath">
		/// The root directory of the solution that this file belongs to.
		/// </param>
		/// <param name="fullPath">
		/// The absolute path to the file that this item represents.
		/// </param>
		public SubmittableDirectFile(string rootPath, string fullPath)
		{
			this.rootPath = rootPath;
			this.fullPath = fullPath;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the path to this file, relative to its location in the final
		/// submitted package.
		/// </summary>
		public string Filename
		{
			get
			{
				return PathUtils.GetRelativePath(rootPath, fullPath);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the kind of this item (a file).
		/// </summary>
		public SubmittableItemKind Kind
		{
			get
			{
				return SubmittableItemKind.File;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a stream that can be used to read the contents of this file.
		/// </summary>
		/// <returns>
		/// A Stream to read the file contents from.
		/// </returns>
		public Stream GetStream()
		{
			return new FileStream(fullPath, FileMode.Open, FileAccess.Read);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// An empty enumeration, since a file has no children.
		/// </summary>
		public IEnumerable<ISubmittableItem> Children
		{
			get
			{
				yield break;
			}
		}


		// ==== Fields ========================================================

		// The root path of the solution that contains this file.
		private string rootPath;

		// The absolute path to the file.
		private string fullPath;
	}
}
