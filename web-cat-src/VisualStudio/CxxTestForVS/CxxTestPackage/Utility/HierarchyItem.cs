/*==========================================================================*\
 |  $Id: HierarchyItem.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;
using System.Drawing;
using System.IO;
using WebCAT.CxxTest.Utility;

namespace WebCAT.CxxTest.VisualStudio.Utility
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Wraps an IVsHierarchy pointer and item ID pair that represents a
	/// folder, file, or other resource in a Visual Studio project and greatly
	/// simplifies access to item properties and children.
	/// </summary>
	class HierarchyItem
	{
		//~ Constructors .....................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a HierarchyItem from the given hierarchy interface and a
		/// root item ID.
		/// </summary>
		/// <param name="hier">
		/// The hierarchy interface to wrap.
		/// </param>
		public HierarchyItem(IVsHierarchy hier) :
			this(hier, VSConstants.VSITEMID_ROOT)
		{
		}


		// ------------------------------------------------------
		/// <summary>
		/// Creates a HierarchyItem from the given hierarchy and item ID.
		/// </summary>
		/// <param name="hier">
		/// The hierarchy interface to wrap.
		/// </param>
		/// <param name="id">
		/// The item ID in the specified hierarchy.
		/// </param>
		public HierarchyItem(IVsHierarchy hier, uint id)
		{
			hierarchy = hier;
			itemID = id;
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets the underlying IVsHierarchy interface for this hierarchy item.
		/// </summary>
		public IVsHierarchy Hierarchy
		{
			get
			{
				return hierarchy;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the underlying item ID for this hierarchy item.
		/// </summary>
		public uint ItemID
		{
			get
			{
				return itemID;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the parent of this hierarchy item.
		/// </summary>
		public HierarchyItem Parent
		{
			get
			{
				if (itemID != VSConstants.VSITEMID_ROOT)
				{
					return new HierarchyItem(hierarchy);
				}
				else
				{
					IVsHierarchy parentHier = GetProperty(
						__VSHPROPID.VSHPROPID_ParentHierarchy)
						as IVsHierarchy;

					if (parentHier == null)
					{
						return null;
					}
					else
					{
						uint parentId = (uint)(int)
							GetProperty(__VSHPROPID.VSHPROPID_Parent);

						if (parentId == VSConstants.VSITEMID_NIL)
							return null;
						else
							return new HierarchyItem(parentHier, parentId);
					}
				}
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the canonical name of this hierarchy item.
		/// </summary>
		public string CanonicalName
		{
			get
			{
				string name;
				hierarchy.GetCanonicalName(itemID, out name);
				return name;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether this hierarchy item represents the
		/// root of its hierarchy.
		/// </summary>
		public bool IsRoot
		{
			get
			{
				return (itemID == VSConstants.VSITEMID_ROOT);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether this hierarchy item represents the
		/// root of its hierarchy and also represents a Visual Studio project.
		/// </summary>
		public bool IsProject
		{
			get
			{
				return IsRoot && (hierarchy is IVsProject);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the displayable caption of this hierarchy item.
		/// </summary>
		public string Caption
		{
			get
			{
				return GetProperty(__VSHPROPID.VSHPROPID_Caption) as string;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the automation/extensibility (DTE) object that corresponds to
		/// this hierarchy item.
		/// </summary>
		public object ExtObject
		{
			get
			{
				return GetProperty(__VSHPROPID.VSHPROPID_ExtObject);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the name of this hierarchy item.
		/// </summary>
		public string Name
		{
			get
			{
				return GetProperty(__VSHPROPID.VSHPROPID_Name) as string;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the full path to the project directory for this hierarchy
		/// item.
		/// </summary>
		public string ProjectDirectory
		{
			get
			{
				return GetProperty(__VSHPROPID.VSHPROPID_ProjectDir)
					as string;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the project ID GUID for this hierarchy item.
		/// </summary>
		public Guid ProjectIDGuid
		{
			get
			{
				return GetGuidProperty(__VSHPROPID.VSHPROPID_ProjectIDGuid);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the type GUID for this hierarchy item.
		/// </summary>
		public Guid TypeGuid
		{
			get
			{
				return GetGuidProperty(__VSHPROPID.VSHPROPID_TypeGuid);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the type name for this hierarchy item.
		/// </summary>
		public string TypeName
		{
			get
			{
				return GetProperty(__VSHPROPID.VSHPROPID_TypeName) as string;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the icon associated with this hierarchy item.
		/// </summary>
		public Icon Icon
		{
			get
			{
				IntPtr ilHandle = new IntPtr((int)GetProperty(
					__VSHPROPID.VSHPROPID_IconImgList));

				int imageIndex = (int)GetProperty(
					__VSHPROPID.VSHPROPID_IconIndex);

				IntPtr iconHandle = NativeMethods.ImageList_GetIcon(
					ilHandle, imageIndex, 0);

				Icon icon = (Icon)Icon.FromHandle(iconHandle).Clone();
				NativeMethods.DestroyIcon(iconHandle);
				return icon;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Supports enumeration of the children of this hierarchy item.
		/// </summary>
		public IEnumerable<HierarchyItem> Children
		{
			get
			{
				// Get the ID number of the first child in the hierarchy.

				object childID;
				hierarchy.GetProperty(itemID,
					(int)__VSHPROPID.VSHPROPID_FirstChild, out childID);

				while ((uint)(int)childID != VSConstants.VSITEMID_NIL)
				{
					// Yield a new hierarchy item representing the current
					// element in the iteration.

					yield return new HierarchyItem(hierarchy,
						(uint)(int)childID);

					// Get the next sibling for the next iteration through the
					// loop.

					hierarchy.GetProperty((uint)(int)childID,
						(int)__VSHPROPID.VSHPROPID_NextSibling, out childID);
				}
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the hierarchy item representing the project that contains
		/// this hierarchy item.
		/// </summary>
		public HierarchyItem ContainingProject
		{
			get
			{
				HierarchyItem item = this.Parent;

				while (item != null && !(item.hierarchy is IVsProject))
					item = item.Parent;

				return item;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the path to this hierarchy item, relative to the directory of
		/// the project that contains it.
		/// </summary>
		public string ProjectRelativePath
		{
			get
			{
				HierarchyItem projectItem = this.ContainingProject;
				IVsProject project = projectItem.Hierarchy as IVsProject;

				string projectDir = projectItem.ProjectDirectory;

				string itemPath;
				project.GetMkDocument(itemID, out itemPath);

				return PathUtils.RelativePathTo(projectDir, itemPath);
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Returns true if two hierarchy items are the same (that is, they
		/// contain the same hierarchy interface and item ID).
		/// </summary>
		/// <param name="obj">
		/// The object to compare to this hierarchy item.
		/// </param>
		/// <returns>
		/// True if the hierarchy items are the same; false if they are not, or
		/// if the given object is not a hierarchy item.
		/// </returns>
		public override bool Equals(object obj)
		{
			HierarchyItem rhs = obj as HierarchyItem;

			if (rhs != null)
				return CanonicalName == rhs.CanonicalName;
			else
				return false;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a hash code that represents this hierarchy item.
		/// </summary>
		/// <returns>
		/// The hash code for this hierarchy item.
		/// </returns>
		public override int GetHashCode()
		{
			return hierarchy.GetHashCode() | itemID.GetHashCode();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the automation/extensibility (DTE) object associated with
		/// this hierarchy item, cast to the specified type.
		/// </summary>
		/// <typeparam name="T">
		/// The type to cast the extensibility object to.
		/// </typeparam>
		/// <returns>
		/// The extensibility object cast to the specified type.
		/// </returns>
		public T GetExtObjectAs<T>() where T : class
		{
			return ExtObject as T;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the hierarchy item that is a descendant of this item, which
		/// has the specified name.
		/// </summary>
		/// <param name="name">
		/// The name of the descendant item to find.
		/// </param>
		/// <returns>
		/// The hierarchy item with the specified name, or null if none was
		/// found.
		/// </returns>
		public HierarchyItem GetChildWithName(string name)
		{
			foreach (HierarchyItem child in
				new DepthFirstTraversal<HierarchyItem>(Children,
				delegate(HierarchyItem item) { return item.Children; }))
			{
				if (child.Name == name)
					return child;
			}

			return null;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the value of a property of this hierarchy item.
		/// </summary>
		/// <param name="prop">
		/// The property to obtain.
		/// </param>
		/// <returns>
		/// The value of the property.
		/// </returns>
		private object GetProperty(__VSHPROPID prop)
		{
			object result;
			hierarchy.GetProperty(itemID, (int)prop, out result);
			return result;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the value of a GUID property of this hierarchy item.
		/// </summary>
		/// <param name="prop">
		/// The property to obtain.
		/// </param>
		/// <returns>
		/// The value of the property.
		/// </returns>
		private Guid GetGuidProperty(__VSHPROPID prop)
		{
			Guid result;
			hierarchy.GetGuidProperty(itemID, (int)prop, out result);
			return result;
		}


		//~ Instance variables ...............................................

		// The underlying hierarchy represented by this hierarchy item.
		private IVsHierarchy hierarchy;

		// The item ID of the item represented by this hierarchy item.
		private uint itemID;
	}
}
