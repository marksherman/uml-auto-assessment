/*==========================================================================*\
 |  $Id: HierarchyItem.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;
using System.Drawing;

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// Wraps an IVsHierarchy pointer and item ID pair that represents a
	/// folder, file, or other resource in a Visual Studio project and greatly
	/// simplifies access to item properties and children.
	/// </summary>
	class HierarchyItem
	{
		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

			if(rhs != null)
			{
				return (hierarchy == rhs.hierarchy) &&
					(itemID == rhs.itemID);
			}
			else
			{
				return false;
			}
		}

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
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

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the value of a property of this hierarchy item.
		/// </summary>
		/// <remarks>
		/// The name of the property is derived from the values in the
		/// __VSHPROPID enumeration by removing the VSHPROPID_ prefix. For
		/// example, to get the value of the __VSHPROPID.VSHPROPID_Name
		/// property, you would pass "Name" as the property parameter to this
		/// accessor.
		/// 
		/// If the value of the property to be accessed is a GUID, use the
		/// GetGuid method instead of this accessor.
		/// </remarks>
		/// <param name="property">
		/// The name of the property to be accessed.
		/// </param>
		/// <returns>
		/// The value of the property.
		/// </returns>
		public object this[string property]
		{
			get
			{
				object result;
				__VSHPROPID propID =
					(__VSHPROPID)Enum.Parse(typeof(__VSHPROPID),
					"VSHPROPID_" + property, true);
				
				hierarchy.GetProperty(itemID, (int)propID, out result);
				return result;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a GUID value of a property of this hierarchy item.
		/// </summary>
		/// <remarks>
		/// See the description of the indexing operator to see how the names
		/// for the properties are derived.
		/// </remarks>
		/// <param name="property">
		/// The name of the property to be accessed.
		/// </param>
		/// <returns>
		/// The value of the property, as a Guid.
		/// </returns>
		public Guid GetGuid(string property)
		{
			Guid result;
			__VSHPROPID propID =
				(__VSHPROPID)Enum.Parse(typeof(__VSHPROPID),
				"VSHPROPID_" + property, true);
			hierarchy.GetGuidProperty(itemID, (int)propID, out result);
			return result;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the icon associated with this hierarchy item.
		/// </summary>
		public Icon Icon
		{
			get
			{
				IntPtr ilHandle = new IntPtr((int)this["IconImgList"]);
				int imageIndex = (int)this["IconIndex"];

				IntPtr iconHandle = NativeMethods.ImageList_GetIcon(
					ilHandle, imageIndex, 0);

				Icon icon = (Icon)Icon.FromHandle(iconHandle).Clone();
				NativeMethods.DestroyIcon(iconHandle);
				return icon;
			}
		}

		//  -------------------------------------------------------------------
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

		// The underlying hierarchy represented by this hierarchy item.
		private IVsHierarchy hierarchy;

		// The item ID of the item represented by this hierarchy item.
		private uint itemID;
	}
}
