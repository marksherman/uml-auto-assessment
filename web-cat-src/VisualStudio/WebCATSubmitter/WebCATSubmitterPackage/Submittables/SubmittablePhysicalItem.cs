/*==========================================================================*\
 |  $Id: SubmittablePhysicalItem.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using WebCAT.Submitter.VisualStudio.Utility;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;
using WebCAT.Submitter;
using System.IO;

namespace WebCAT.Submitter.VisualStudio.Submittables
{
	/// <summary>
	/// Represents a physical file or folder in a Visual Studio hierarchy.
	/// </summary>
	class SubmittablePhysicalItem : ISubmittableItem
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable physical item.
		/// </summary>
		/// <param name="rootPath">
		/// The root path of the solution that contains this item.
		/// </param>
		/// <param name="project">
		/// The project that contains this item.
		/// </param>
		/// <param name="hierItem">
		/// The hierarchy item that this item represents.
		/// </param>
		public SubmittablePhysicalItem(string rootPath, IVsProject project,
			HierarchyItem hierItem)
		{
			this.rootPath = rootPath;
			this.project = project;
			this.hierarchy = hierItem;
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
				return PathUtils.GetRelativePath(rootPath, FullPath);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the full path to the file or folder on the file system that
		/// this item represents.
		/// </summary>
		internal string FullPath
		{
			get
			{
				string path;
				project.GetMkDocument(hierarchy.ItemID, out path);
				return path;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the kind of this item (file or folder) based on the underlying
		/// hierarchy item type.
		/// </summary>
		public SubmittableItemKind Kind
		{
			get
			{
				Guid guid = hierarchy.GetGuid("TypeGuid");

				if (guid == VSConstants.GUID_ItemType_PhysicalFolder)
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
		/// Gets a stream that can be used to read the contents of this item,
		/// if it is a file.
		/// </summary>
		/// <returns>
		/// A Stream that can be used to read the file, or null if this item
		/// is a folder.
		/// </returns>
		public Stream GetStream()
		{
			if (Kind == SubmittableItemKind.File)
			{
				return new FileStream(
					FullPath, FileMode.Open, FileAccess.Read);
			}
			else
			{
				return null;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Supports enumeration over the children of this item, if it is a
		/// folder. If it is a file, the enumeration is empty.
		/// </summary>
		public IEnumerable<ISubmittableItem> Children
		{
			get
			{
				foreach (HierarchyItem item in hierarchy.Children)
				{
					yield return new SubmittablePhysicalItem(
						rootPath, project, item);
				}
			}
		}


		// ==== Fields ========================================================

		// The root path of the solution that contains this item.
		private string rootPath;

		// The project that contains this item.
		private IVsProject project;

		// The hierarchy item that this submittable item represents.
		private HierarchyItem hierarchy;
	}
}
