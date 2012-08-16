/*==========================================================================*\
 |  $Id: DebuggerEventsHandler.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.Events
{
	// --------------------------------------------------------------------
	/// <summary>
	/// A class that listens for and handles events generated by the Visual
	/// Studio debugger.
	/// </summary>
	class DebuggerEventsHandler : IVsDebuggerEvents
	{
		//~ Properties .......................................................

		// ----------------------------------------------------------------
		/// <summary>
		/// Gets or sets a value indicating that when the debugger is next
		/// invoked, it is because the user started a test run.
		/// </summary>
		public bool StartingTestRun
		{
			get
			{
				return startingTestRun;
			}
			set
			{
				startingTestRun = value;
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Called when the debugger's mode changes.
		/// </summary>
		/// <param name="dbgmodeNew">
		/// The new debugger mode.
		/// </param>
		/// <returns>
		/// S_OK on success, otherwise an error code.
		/// </returns>
		public int OnModeChange(DBGMODE dbgmodeNew)
		{
			if (dbgmodeNew == DBGMODE.DBGMODE_Design)
			{
				if (startingTestRun)
				{
					startingTestRun = false;

					// Update the CxxTest Results tool window once execution
					// is complete.

					CxxTestPackage.Instance.TryToRefreshTestResultsWindow();
				}
			}

			return VSConstants.S_OK;
		}


		//~ Instance variables ...............................................

		// When the debugger is next invoked, it is because the user started a
		// test run.
		private bool startingTestRun;
	}
}
