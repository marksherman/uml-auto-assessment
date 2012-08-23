/*==========================================================================*\
 |  $Id: VsShellUtils.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.Runtime.InteropServices;
using System.Drawing;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// This class contains various methods that greatly simplify the use of
	/// Visual Studio shell interfaces.
	/// </summary>
	static class VsShellUtils
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Converts an interface pointer in IntPtr form to the specified .NET
		/// interface type.
		/// </summary>
		/// <typeparam name="T">
		/// The type of the interface that the pointer should be cast to.
		/// </typeparam>
		/// <param name="ptr">
		/// The interface pointer that should be cast.
		/// </param>
		/// <returns>
		/// A reference to the interface that was requested.
		/// </returns>
		public static T IntPtrToInterface<T>(IntPtr ptr) where T : class
		{
			if (ptr != IntPtr.Zero)
			{
				T iface = (T)Marshal.GetObjectForIUnknown(ptr);
				Marshal.Release(ptr);
				return iface;
			}
			else
			{
				return null;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets an array of hierarchy items that represent the currently
		/// selected items in the Solution Explorer.
		/// </summary>
		/// <param name="provider">
		/// The DTE object to use as a service provider.
		/// </param>
		/// <returns>
		/// An array of HierarchyItem objects that are currently selected.
		/// </returns>
		public static HierarchyItem[] GetCurrentSelection(
			IServiceProvider provider)
		{
			IVsMonitorSelection vsms = (IVsMonitorSelection)
				provider.GetService(typeof(SVsShellMonitorSelection));

			IntPtr hierPtr;
			uint itemID;
			IVsMultiItemSelect multiSel;
			IntPtr selCont;

			vsms.GetCurrentSelection(out hierPtr, out itemID, out multiSel,
				out selCont);

			if (hierPtr != IntPtr.Zero)
			{
				// Single selection

				IVsHierarchy hier = IntPtrToInterface<IVsHierarchy>(hierPtr);

				return new HierarchyItem[] {
					new HierarchyItem(hier, itemID)
				};
			}
			else if(itemID == VSConstants.VSITEMID_SELECTION)
			{
				// Multiple selection

				uint count;
				int isSingle;
				multiSel.GetSelectionInfo(out count, out isSingle);

				VSITEMSELECTION[] selection = new VSITEMSELECTION[count];
				multiSel.GetSelectedItems(0, count, selection);

				HierarchyItem[] hierItems = new HierarchyItem[count];
				int i = 0;
				foreach (VSITEMSELECTION selectedItem in selection)
				{
					hierItems[i++] = new HierarchyItem(selectedItem.pHier,
						selectedItem.itemid);
				}

				return hierItems;
			}
			else
			{
				return new HierarchyItem[] { };
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the current solution that is loaded into this instance of
		/// Visual Studio.
		/// </summary>
		/// <param name="dte">
		/// The DTE object through which the solution interface will be
		/// obtained.
		/// </param>
		/// <returns>
		/// An IVsSolution interface that represents the currently loaded
		/// solution.
		/// </returns>
		public static IVsSolution GetSolution(IServiceProvider sp)
		{
			return (IVsSolution)sp.GetService(typeof(SVsSolution));
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Supports enumeration of the currently loaded projects in the
		/// current instance of Visual Studio.
		/// </summary>
		/// <param name="dte">
		/// The DTE object through which the solution will be accessed.
		/// </param>
		/// <returns>
		/// An enumeration of the currently loaded projects.
		/// </returns>
		public static IEnumerable<HierarchyItem>
			GetLoadedProjects(IServiceProvider sp)
		{
			return GetLoadedProjects(GetSolution(sp));
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Supports enumeration of the currently loaded projects in the given
		/// solution.
		/// </summary>
		/// <param name="solution">
		/// The solution whose projects you wish to enumerate.
		/// </param>
		/// <returns>
		/// An enumeration of the currently loaded projects.
		/// </returns>
		public static IEnumerable<HierarchyItem>
			GetLoadedProjects(IVsSolution solution)
		{
			IEnumHierarchies enumerator;
			Guid dummyGuid = Guid.Empty;

			solution.GetProjectEnum(
				(uint)__VSENUMPROJFLAGS.EPF_LOADEDINSOLUTION, ref dummyGuid,
				out enumerator);

			IVsHierarchy[] array = new IVsHierarchy[1];
			uint fetched;

			enumerator.Next(1, array, out fetched);
			while (fetched > 0)
			{
				yield return new HierarchyItem(array[0]);
				enumerator.Next(1, array, out fetched);
			}
		}
	}
}
