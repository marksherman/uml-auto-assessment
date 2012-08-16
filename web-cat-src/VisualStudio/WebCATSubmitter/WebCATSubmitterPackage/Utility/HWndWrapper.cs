/*==========================================================================*\
 |  $Id: HWndWrapper.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// A class that wraps a Win32 HWND handle as an IWin32Window interface
	/// reference so that it can be used as the owner of Windows Forms window.
	/// </summary>
	class HWndWrapper : System.Windows.Forms.IWin32Window
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Wraps the specified HWND handle.
		/// </summary>
		/// <param name="hwnd">
		/// The window handle to be wrapped.
		/// </param>
		public HWndWrapper(IntPtr hwnd)
		{
			handle = hwnd;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the window handle wrapped by this object.
		/// </summary>
		public IntPtr Handle
		{
			get
			{
				return handle;
			}
		}

		// The handle being wrapped.
		private IntPtr handle;
	}
}
