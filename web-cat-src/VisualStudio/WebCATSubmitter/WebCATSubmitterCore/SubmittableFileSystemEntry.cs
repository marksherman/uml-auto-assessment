/*==========================================================================*\
 |  $Id: SubmittableFileSystemEntry.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission engine for the
 |	.NET framework.
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

namespace WebCAT.Submitter
{
	/// <summary>
	/// A default implementation of the ISubmittableItem interface that
	/// encapsulates directories and files in the file system.
	/// </summary>
	/// <remarks>
	/// This class is provided for users who want a simple way to package up
	/// a directory tree for submission and do not need to implement their own
	/// submittable items to represent a particular project structure in an
	/// IDE.
	/// </remarks>
	public class SubmittableFileSystemEntry : ISubmittableItem
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable file system entry rooted at the specified
		/// path.
		/// </summary>
		/// <param name="fullPath">
		/// The path to the file or directory that will act as the root of this
		/// tree of submittable items.
		/// </param>
		public SubmittableFileSystemEntry(string fullPath) :
			this(null, fullPath)
		{
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable file system entry with the specified item
		/// as its parent, pointing to file at the specified path.
		/// </summary>
		/// <param name="parent">
		/// The file system entry that is the parent of this item.
		/// </param>
		/// <param name="fullPath">
		/// The path to the file or directory that this item will represent.
		/// </param>
		private SubmittableFileSystemEntry(SubmittableFileSystemEntry parent,
			string fullPath)
		{
			this.parent = parent;
			this.fullPath = fullPath;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the filename of this file or directory, relative to its
		/// location in the destination package.
		/// </summary>
		public string Filename
		{
			get
			{
				if (parent == null)
				{
					return "";
				}
				else
				{
					return Path.Combine(parent.Filename,
						Path.GetFileName(fullPath));
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the kind of this submittable item (file or folder).
		/// </summary>
		public SubmittableItemKind Kind
		{
			get
			{
				if (Directory.Exists(fullPath))
				{
					return SubmittableItemKind.Folder;
				}
				else
				{
					return SubmittableItemKind.File;
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a stream that can be used to read from the file represented
		/// by this item, if it is a file.
		/// </summary>
		/// <returns>
		/// A Stream to read the file from, or null if this item represents a
		/// folder.
		/// </returns>
		public Stream GetStream()
		{
			if (Kind == SubmittableItemKind.File)
			{
				return new FileStream(
					fullPath, FileMode.Open, FileAccess.Read);
			}
			else
			{
				return null;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Supports enumeration over the child files and folders in this
		/// file system entry, if it is a folder.
		/// </summary>
		public IEnumerable<ISubmittableItem> Children
		{
			get
			{
				if (Kind == SubmittableItemKind.Folder)
				{
					string[] entries =
						Directory.GetFileSystemEntries(fullPath);

					foreach (string entry in entries)
					{
						yield return
							new SubmittableFileSystemEntry(this, entry);
					}
				}
			}
		}

		// ==== Fields ========================================================

		// The parent file system entry that contains this item. This field is
		// used merely to relativize the path to the file in the destination
		// package by traversing upward to the root, even though each item
		// contains the full absolute path to the file.
		private SubmittableFileSystemEntry parent;

		// The absolute path to the file or directory.
		private string fullPath;
	}
}
