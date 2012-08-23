/*==========================================================================*\
 |  $Id: CxxTestSuitesToolWindow.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using Microsoft.VisualStudio.Shell;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.ComponentModel.Design;
using Microsoft.VisualStudio.Shell.Interop;
using WebCAT.CxxTest.VisualStudio.Utility;
using EnvDTE;
using EnvDTE80;

namespace WebCAT.CxxTest.VisualStudio.ToolWindows
{
	/// <summary>
	/// This class implements the tool window exposed by this package and hosts a user control.
	///
	/// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane, 
	/// usually implemented by the package implementer.
	///
	/// This represents the window that will allow the user to select a test suite to run
	/// </summary>
	[Guid("c2241c57-a91f-48d4-a532-6fe43891e006")]
	public class CxxTestSuitesToolWindow : ToolWindowPane
	{
		// This is the user control hosted by the tool window; it is exposed to the base class 
		// using the Window property. Returns a CxxTestView control.
		internal CxxTestSuitesToolWindowPanel control;

		/// <summary>
		/// Standard constructor for the tool window.
		/// </summary>
		public CxxTestSuitesToolWindow() :
			base(null)
		{
			// Set the window title reading it from the resources.
			this.Caption = Resources.ToolWindowTitle;
			// Set the image that will appear on the tab of the window frame
			// when docked with an other window
			// The resource ID correspond to the one defined in the resx file
			// while the Index is the offset in the bitmap strip. Each image in
			// the strip being 16x16.
			this.BitmapResourceID = 300;
			this.BitmapIndex = 0;

			// Initialize the tool window's toolbar.
			this.ToolBar = new CommandID(Guids.CxxTestPackageCmdSet,
				CommandIds.CxxTestViewToolbar);
			this.ToolBarLocation = (int)VSTWT_LOCATION.VSTWT_TOP;

			// Create the handlers for the toolbar commands.
			OleMenuCommandService mcs = GetService(typeof(IMenuCommandService))
				as OleMenuCommandService;
			
			if (mcs != null)
			{
				CommandID cmdID;
				MenuCommand menuCmd;

				cmdID = new CommandID(Guids.CxxTestPackageCmdSet,
					CommandIds.cmdidRefreshTests);
				menuCmd = new OleMenuCommand(
					RefreshTests_Invoked, delegate { },
					RefreshTests_BeforeQueryStatus, cmdID);
				mcs.AddCommand(menuCmd);

				cmdID = new CommandID(Guids.CxxTestPackageCmdSet,
					CommandIds.cmdidRunTests);
				menuCmd = new OleMenuCommand(
					RunTests_Invoked, delegate {},
					RunTests_BeforeQueryStatus, cmdID);
				mcs.AddCommand(menuCmd);
			}

			control = new CxxTestSuitesToolWindowPanel();
			RefreshFromSolution();
		}


		/// <summary>
		/// This property returns the handle to the user control that should
		/// be hosted in the Tool Window.
		/// </summary>
		override public IWin32Window Window
		{
			get
			{
				return (IWin32Window)control;
			}
		}

		public Dictionary<string, bool> SelectedTestCases
		{
			get
			{
				return control.SelectedTestCases;
			}
		}

		public void RefreshFromSolution()
		{
			control.RefreshFromSolution();
		}

		public void AddElement(CodeElement element)
		{
			try
			{
				control.AddElement(element);
			}
			catch (Exception)
			{
				// To protect against unforeseen problems, we'll just do a
				// global refresh if anything bad happens during the update.
				control.RefreshFromSolution();
			}
		}

		public void ChangeElement(CodeElement element, vsCMChangeKind changeKind)
		{
			try
			{
				control.ChangeElement(element, changeKind);
			}
			catch (Exception)
			{
				// To protect against unforeseen problems, we'll just do a
				// global refresh if anything bad happens during the update.
				control.RefreshFromSolution();
			}
		}

		public void DeleteElement(object parent, CodeElement element)
		{
			try
			{
				control.DeleteElement(parent, element);
			}
			catch (Exception)
			{
				// To protect against unforeseen problems, we'll just do a
				// global refresh if anything bad happens during the update.
				control.RefreshFromSolution();
			}
		}

		private bool SolutionExists
		{
			get
			{
				IVsSolution solution = VsShellUtils.GetSolution(
					CxxTestPackage.Instance);
				HierarchyItem hier = new HierarchyItem(solution as IVsHierarchy);

				return (hier.Name != null);
			}
		}

		private void RefreshTests_BeforeQueryStatus(object sender, EventArgs e)
		{
			OleMenuCommand command = (OleMenuCommand)sender;
//			command.Enabled = SolutionExists;

//			if (command.Enabled)
//				RefreshFromSolution();
		}

		private void RefreshTests_Invoked(object sender, EventArgs e)
		{
			control.RefreshFromSolution();
		}

		private void RunTests_BeforeQueryStatus(object sender, EventArgs e)
		{
			OleMenuCommand command = (OleMenuCommand)sender;
//			command.Enabled = SolutionExists;
		}

		private void RunTests_Invoked(object sender, EventArgs e)
		{
			control.RefreshFromSolution();
			CxxTestPackage.Instance.CreateTestRunnerFile(false);
			CxxTestPackage.Instance.BuildSolutionToRunTests();
		}
	}
}
