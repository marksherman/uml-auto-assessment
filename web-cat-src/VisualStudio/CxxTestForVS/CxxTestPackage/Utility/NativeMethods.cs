/*==========================================================================*\
 |  $Id: NativeMethods.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using System.Runtime.InteropServices;

namespace WebCAT.CxxTest.VisualStudio.Utility
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Contains any native methods that are imported through interop services.
	/// </summary>
	internal static class NativeMethods
	{
		// --------------------------------------------------------------------
		// Since the Windows Forms ImageList class does not permit us to
		// create an imagelist from a Win32 HIMAGELIST handle, we import this
		// method instead to access an icon from the handle directly.
		[DllImport("comctl32.dll")]
		public static extern IntPtr ImageList_GetIcon(IntPtr hImageList,
			int i, uint flags);


		// --------------------------------------------------------------------
		// Destroy the icon that was returned by ImageList_GetIcon.
		[DllImport("user32.dll")]
		public static extern int DestroyIcon(IntPtr hIcon);

		
		// --------------------------------------------------------------------
		// Used to obtain relative paths between two files or directories.
		[DllImport("shlwapi.dll")]
		public static extern bool PathRelativePathTo(StringBuilder pszPath,
			string pszFrom, uint dwAttrFrom, string pszTo, uint dwAttrTo);

		// Constants used by PathRelativePathTo.
		public const uint FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
		public const uint FILE_ATTRIBUTE_NORMAL = 0x00000080;
	}
}
