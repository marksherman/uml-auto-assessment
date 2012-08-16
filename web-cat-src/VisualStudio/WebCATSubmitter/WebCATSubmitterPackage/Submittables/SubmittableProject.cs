/*==========================================================================*\
 |  $Id: SubmittableProject.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.IO;
using WebCAT.Submitter.VisualStudio.Utility;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;

namespace WebCAT.Submitter.VisualStudio.Submittables
{
	/// <summary>
	/// A submittable item that represents a project in a Visual Studio
	/// solution.
	/// </summary>
	class SubmittableProject : ISubmittableItem
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable project item.
		/// </summary>
		/// <param name="rootPath">
		/// The root path of the solution that contains this project.
		/// </param>
		/// <param name="hierItem">
		/// The hierarchy item that represents this project.
		/// </param>
		public SubmittableProject(string rootPath, HierarchyItem hierItem)
		{
			this.rootPath = rootPath;
			this.hierarchy = hierItem;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the hierarchy item that this submittable item represents.
		/// </summary>
		internal HierarchyItem HierarchyItem
		{
			get
			{
				return hierarchy;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the relative path of the project -directory-, not of the
		/// project description file (this is yielded as a file inside the
		/// Children property).
		/// </summary>
		public string Filename
		{
			get
			{
				return PathUtils.GetRelativePath(rootPath,
					Path.GetDirectoryName(FullPath)) +
					Path.DirectorySeparatorChar;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the full path to the project description file (.csproj,
		/// .vcproj, etc.) of this project.
		/// </summary>
		internal string FullPath
		{
			get
			{
				string name;
				IVsProject project = (IVsProject)hierarchy.Hierarchy;
				project.GetMkDocument(VSConstants.VSITEMID_ROOT, out name);
				return name;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the kind of this item (a folder, since it represents the
		/// folder that contains the files in the project).
		/// </summary>
		public SubmittableItemKind Kind
		{
			get
			{
				return SubmittableItemKind.Folder;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns null since a project item represents the folder that
		/// contains the project.
		/// </summary>
		/// <returns>
		/// Returns null.
		/// </returns>
		public Stream GetStream()
		{
			return null;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Enumerates the children (physical files and folders) of this
		/// project.
		/// </summary>
		public IEnumerable<ISubmittableItem> Children
		{
			get
			{
				// Yield the project description file (.csproj, .vcproj, etc).
				yield return new SubmittableDirectFile(rootPath, FullPath);

				foreach (HierarchyItem item in hierarchy.Children)
				{
					Guid guid = item.GetGuid("TypeGuid");

					if (guid == VSConstants.GUID_ItemType_PhysicalFolder ||
						guid == VSConstants.GUID_ItemType_PhysicalFile)
					{
						yield return new SubmittablePhysicalItem(rootPath,
							(IVsProject)hierarchy.Hierarchy, item);
					}
				}
			}
		}


		// ==== Fields ========================================================

		// The root path of the solution that contains this item.
		private string rootPath;

		// The hierarchy item that this submittable item represents.
		private HierarchyItem hierarchy;
	}
}
