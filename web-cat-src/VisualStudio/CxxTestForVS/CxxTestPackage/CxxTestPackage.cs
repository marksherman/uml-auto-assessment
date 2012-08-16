/*==========================================================================*\
 |  $Id: CxxTestPackage.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
using System.Diagnostics;
using System.Globalization;
using System.Runtime.InteropServices;
using System.ComponentModel.Design;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;
using Microsoft.Win32;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;
using Microsoft.VisualStudio.OLE.Interop;
using Microsoft.VisualStudio.Shell;
using Microsoft.VisualStudio.TextManager.Interop;
using Microsoft.VisualStudio.VCCodeModel;
using Microsoft.VisualStudio.VCProject;
using Microsoft.VisualStudio.VCProjectEngine;
using System.Threading;
using EnvDTE;
using WebCAT.CxxTest.VisualStudio.Model;
using WebCAT.CxxTest.VisualStudio.Utility;
using WebCAT.CxxTest.VisualStudio.Forms;
using EnvDTE80;
using WebCAT.CxxTest.VisualStudio.ToolWindows;
using System.Collections;
using System.Xml;
using WebCAT.CxxTest.VisualStudio.ResultModel;
using WebCAT.CxxTest.VisualStudio.Templating;
using WebCAT.CxxTest.VisualStudio.Events;
using System.Reflection;


namespace WebCAT.CxxTest.VisualStudio
{
	// --------------------------------------------------------------------
    /// <summary>
    /// Implements the Visual Studio Integration Package that provides CxxTest
	/// unit testing support for Visual Studio.
    /// </summary>
    [PackageRegistration(UseManagedResourcesOnly = true)]
    [DefaultRegistryRoot("Software\\Microsoft\\VisualStudio\\8.0")]
    [InstalledProductRegistration(
		false, "#110", "#112", "1.0", IconResourceID = 400)]
    [ProvideLoadKey(
		"Standard", "1.0", "CxxTest for Visual Studio",
		"Web-CAT Development Team", 1)]
    [ProvideMenuResource(1000, 1)]
    [ProvideToolWindow(typeof(CxxTestSuitesToolWindow))]
	[ProvideToolWindow(typeof(CxxTestResultsToolWindow))]
	[ProvideAutoLoad("f1536ef8-92ec-443c-9ed7-fdadf150da82")]
	[Guid(Guids.CxxTestPackageString)]
    public sealed class CxxTestPackage : Package
    {
		//~ Constructor ......................................................

		// ------------------------------------------------------
        /// <summary>
		/// Creates a new instance of the CxxTestPackage class.
        /// </summary>
        public CxxTestPackage()
        {
            instance = this;
        }


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets the sole instance of the CxxTestPackage class.
		/// </summary>
		public static CxxTestPackage Instance
		{
			get
			{
				return instance;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the collection of all test suites found in the currently
		/// loaded solution.
		/// </summary>
		internal TestSuiteCollection AllTestSuites
		{
			get
			{
				TestSuiteCollection suites = new TestSuiteCollection(
					VsShellUtils.GetSolution(this));

				suites.PopulateFromSolution();
				return suites;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the hierarchy item representing the last project that was
		/// executed.
		/// </summary>
		internal HierarchyItem LastRunProject
		{
			get
			{
				return lastRunProject;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the test suite results of the last run.
		/// </summary>
		internal TestSuiteResult[] SuiteResultsOfLastRun
		{
			get
			{
				HierarchyItem startupProject = lastRunProject;

				if (startupProject == null)
					return new TestSuiteResult[0];

				string projectDir = startupProject.ProjectDirectory;
				string workingDir = GetActiveWorkingDirectory(startupProject);

				string resultsPath = Path.Combine(workingDir,
					Constants.TestResultsFilename);

				try
				{
					TestResultsProcessor results =
						new TestResultsProcessor(resultsPath);
					return results.SuiteResults;
				}
				catch (Exception)
				{
					return null;
				}
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the Dereferee results of the last run.
		/// </summary>
		internal DerefereeResults DerefereeResultsOfLastRun
		{
			get
			{
				HierarchyItem startupProject = lastRunProject;

				if (startupProject == null)
					return new DerefereeResults();

				string projectDir = startupProject.ProjectDirectory;
				string workingDir = GetActiveWorkingDirectory(startupProject);

				string resultsPath = Path.Combine(workingDir,
					Constants.MemoryResultsFilename);

				try
				{
					DerefereeProcessor proc =
						new DerefereeProcessor(resultsPath);
					return proc.DerefereeResults;
				}
				catch (Exception)
				{
					return null;
				}
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Called when the CxxTest Suites menu item is selected.
        /// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
        private void ShowCxxTestSuitesWindow_Invoked(object sender,
			EventArgs e)
        {
			// Get the 0th (only) instance of the tool window. The final
			// argument indicates that the window should be created if it does
			// not exist already.

			ToolWindowPane window = FindToolWindow(
				typeof(CxxTestSuitesToolWindow), 0, true);
            
			if (window == null || window.Frame == null)
				throw new COMException(Resources.CanNotCreateWindow);

			IVsWindowFrame windowFrame = (IVsWindowFrame)window.Frame;
            ErrorHandler.ThrowOnFailure(windowFrame.Show());
        }


		// ------------------------------------------------------
		/// <summary>
		/// Called when the CxxTest Results menu item is selected.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		private void ShowCxxTestResultsWindow_Invoked(object sender,
			EventArgs e)
		{
			// Get the 0th (only) instance of the tool window. The final
			// argument indicates that the window should be created if it does
			// not exist already.

			ToolWindowPane window = FindToolWindow(
				typeof(CxxTestResultsToolWindow), 0, true);

			if (window == null || window.Frame == null)
				throw new COMException(Resources.CanNotCreateWindow);

			IVsWindowFrame windowFrame = (IVsWindowFrame)window.Frame;
			ErrorHandler.ThrowOnFailure(windowFrame.Show());
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to refresh the CxxTest Suites window with the test suites
		/// currently in the solution. Does nothing if the window is not yet
		/// created.
		/// </summary>
		public void TryToRefreshTestSuitesWindow()
		{
			CxxTestSuitesToolWindow window =
				FindToolWindow(typeof(CxxTestSuitesToolWindow), 0, false)
				as CxxTestSuitesToolWindow;

			if (window != null)
				window.RefreshFromSolution();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to add the specified code element to the CxxTest Suites
		/// window. Does nothing if the window is not yet created.
		/// </summary>
		/// <param name="element">
		/// The VC code model element to be added.
		/// </param>
		public void TryToAddElementToTestSuitesWindow(CodeElement element)
		{
			CxxTestSuitesToolWindow window =
				FindToolWindow(typeof(CxxTestSuitesToolWindow), 0, false)
				as CxxTestSuitesToolWindow;

			if (window != null)
				window.AddElement(element);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to change the specified element in the CxxTest Suites
		/// window. Does nothing if the window is not yet created.
		/// </summary>
		/// <param name="element">
		/// The element that was changed.
		/// </param>
		/// <param name="changeKind">
		/// The kind of change that occurred.
		/// </param>
		public void TryToChangeElementToTestSuitesWindow(CodeElement element,
			vsCMChangeKind changeKind)
		{
			CxxTestSuitesToolWindow window =
				FindToolWindow(typeof(CxxTestSuitesToolWindow), 0, false)
				as CxxTestSuitesToolWindow;

			if (window != null)
				window.ChangeElement(element, changeKind);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to delete the specified element in the CxxTest Suites
		/// window. Does nothing if the window is not yet created.
		/// </summary>
		/// <param name="parent">
		/// The parent of the element that was deleted.
		/// </param>
		/// <param name="element">
		/// The element that was deleted.
		/// </param>
		public void TryToDeleteElementToTestSuitesWindow(object parent,
			CodeElement element)
		{
			CxxTestSuitesToolWindow window =
				FindToolWindow(typeof(CxxTestSuitesToolWindow), 0, false)
				as CxxTestSuitesToolWindow;

			if (window != null)
				window.DeleteElement(parent, element);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to refresh the CxxTest Results window based on the results
		/// of the last run. Does nothing if the window is not yet created.
		/// </summary>
		public void TryToRefreshTestResultsWindow()
		{
			CxxTestResultsToolWindow window =
				FindToolWindow(typeof(CxxTestResultsToolWindow), 0, false)
				as CxxTestResultsToolWindow;

			if (window != null)
				window.RefreshFromLastRun();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Tries to refresh the CxxTest Results window by setting the
		/// specified text message in the pass/fail bar. Does nothing if the
		/// window is not yet created.
		/// </summary>
		/// <param name="text">
		/// The message to be displayed in the pass/fail bar.
		/// </param>
		public void TryToSetTestResultsText(string text)
		{
			CxxTestResultsToolWindow window =
				FindToolWindow(typeof(CxxTestResultsToolWindow), 0, false)
				as CxxTestResultsToolWindow;

			if (window != null)
				window.SetToTextMode(text);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Brings the error list window to the front.
		/// </summary>
		public void BringErrorListToFront()
		{
			IVsErrorList errorList =
				(IVsErrorList)GetService(typeof(SVsErrorList));

			errorList.BringToFront();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Hooks the specified delegates into the code model event sink.
		/// </summary>
		/// <param name="elementAdded">
		/// A delegate to be called when an element is added to the code
		/// model.
		/// </param>
		/// <param name="elementChanged">
		/// A delegate to be called when an element changes in the code model.
		/// </param>
		/// <param name="elementDeleted">
		/// A delegate to be called when an element is deleted from the code
		/// model.
		/// </param>
		public void AdviseCodeModelEvents(
			_dispCodeModelEvents_ElementAddedEventHandler elementAdded,
			_dispCodeModelEvents_ElementChangedEventHandler elementChanged,
			_dispCodeModelEvents_ElementDeletedEventHandler elementDeleted)
		{
			DTE dte = (DTE)GetService(typeof(DTE));
			Events2 events = (Events2)((DTE2)dte).Events;
			CodeModelEvents cmEvents = events.get_CodeModelEvents(null);

			if (cmEvents != null)
			{
				if (elementAdded != null)
					cmEvents.ElementAdded += elementAdded;

				if (elementChanged != null)
					cmEvents.ElementChanged += elementChanged;

				if (elementDeleted != null)
					cmEvents.ElementDeleted += elementDeleted;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Unhooks the specified delegates from the code model event sink.
		/// </summary>
		/// <param name="elementAdded">
		/// A delegate that was previously registered to be called when an
		/// element is added to the code model.
		/// </param>
		/// <param name="elementChanged">
		/// A delegate that was previously registered to be called when an
		/// element changes in the code model.
		/// </param>
		/// <param name="elementDeleted">
		/// A delegate that was previously registered to be called when an
		/// element is deleted from the code model.
		/// </param>
		public void UnadviseCodeModelEvents(
			_dispCodeModelEvents_ElementAddedEventHandler elementAdded,
			_dispCodeModelEvents_ElementChangedEventHandler elementChanged,
			_dispCodeModelEvents_ElementDeletedEventHandler elementDeleted)
		{
			// This might be called as a consequence of closing the IDE, in
			// which case this would fail, so wrap it in a try/catch.

			try
			{
				DTE dte = (DTE)GetService(typeof(DTE));
				Events2 events = (Events2)((DTE2)dte).Events;
				CodeModelEvents cmEvents = events.get_CodeModelEvents(null);

				if (cmEvents != null)
				{
					if (elementAdded != null)
						cmEvents.ElementAdded -= elementAdded;

					if (elementChanged != null)
						cmEvents.ElementChanged -= elementChanged;

					if (elementDeleted != null)
						cmEvents.ElementDeleted -= elementDeleted;
				}
			}
			catch (Exception) { }
		}


		// ------------------------------------------------------
		/// <summary>
		/// Asks the CxxTest Suites tool window which test cases should be
		/// run, based on the user's selection.
		/// </summary>
		/// <returns>
		/// A dictionary containing all of the test cases currently available
		/// in the solution. The keys are the C++ qualified names of the test
		/// cases (TestClass::TestMethod), and the values are a boolean
		/// indicating whether or not the test should be run.
		/// </returns>
		public Dictionary<string, bool> TryToGetTestsToRun()
		{
			CxxTestSuitesToolWindow window =
				FindToolWindow(typeof(CxxTestSuitesToolWindow), 0, false)
				as CxxTestSuitesToolWindow;

			if (window != null)
				return window.SelectedTestCases;
			else
				return new Dictionary<string, bool>();
		}


		// ------------------------------------------------------
		/// <summary>
        /// This method is called after the package is sited to perform any
		/// initialization that relies on Visual Studio services.
        /// </summary>
        protected override void Initialize()
        {
            base.Initialize();

            // Add the command handlers for the menus and toolbars.

            OleMenuCommandService mcs = GetService(
				typeof(IMenuCommandService)) as OleMenuCommandService;

			if (mcs != null)
            {
				CommandID cmdID;

				// View / Other Windows / CxxTest Suites

				cmdID = new CommandID(Guids.CxxTestPackageCmdSet,
					(int)CommandIds.cmdidCxxTestViewWindow);
                mcs.AddCommand(new MenuCommand(
					new EventHandler(ShowCxxTestSuitesWindow_Invoked),
					cmdID));

				// View / Other Windows / CxxTest Results
				
				cmdID = new CommandID(Guids.CxxTestPackageCmdSet,
					(int)CommandIds.cmdidCxxTestResultsWindow);
                mcs.AddCommand(new MenuCommand(
					new EventHandler(ShowCxxTestResultsWindow_Invoked),
					cmdID));

				// <Solution Item> / Generate Test Suite...
				
				cmdID = new CommandID(Guids.CxxTestPackageCmdSet,
					(int)CommandIds.cmdidGenerateTests);
				mcs.AddCommand(new OleMenuCommand(
					new EventHandler(GenerateTests_Invoked),
					delegate { },
					new EventHandler(GenerateTests_BeforeQueryStatus),
					cmdID));
            }

			// Add the CxxTest and Dereferee includes and libraries paths to
			// the Visual C++ directories options if they're not already
			// there.

			AddIncludesAndLibrariesPaths();

            // Initialize the solution build events listener.

			solutionBuildEvents = new SolutionBuildEventsHandler();

			IVsSolutionBuildManager manager =
				(IVsSolutionBuildManager)GetService(
				typeof(SVsSolutionBuildManager));
            manager.AdviseUpdateSolutionEvents(
				solutionBuildEvents, out solutionBuildCookie);

			// Listen for solution events. (We may want to switch this over to
			// VSIP interfaces in the future.)

			DTE dte = (DTE)GetService(typeof(DTE));
			dte.Events.SolutionEvents.Opened += SolutionEvents_Opened;
			dte.Events.SolutionEvents.ProjectAdded +=
				SolutionEvents_ProjectAdded;
			dte.Events.SolutionEvents.ProjectRemoved +=
				SolutionEvents_ProjectRemoved;
			dte.Events.SolutionEvents.BeforeClosing +=
				SolutionEvents_BeforeClosing;
			dte.Events.SolutionEvents.AfterClosing +=
				SolutionEvents_AfterClosing;

			// Initialize the project documents tracking events handler.

			IVsTrackProjectDocuments2 trackDocs = GetTrackProjectDocuments();
			trackProjectDocsEvents = new TrackProjectDocumentsEventsHandler();
			trackDocs.AdviseTrackProjectDocumentsEvents(
				trackProjectDocsEvents, out trackProjectDocsCookie);

			// Initialize the debugger events handler.

			debuggerEvents = new DebuggerEventsHandler();
			
			IVsDebugger debugger =
				(IVsDebugger)GetService(typeof(SVsShellDebugger));
			debugger.AdviseDebuggerEvents(debuggerEvents, out debuggerCookie);
        }


		// ------------------------------------------------------
		/// <summary>
		/// Add the CxxTest and Dereferee paths to the Visual C++ settings,
		/// if they aren't already there.
		/// </summary>
		private void AddIncludesAndLibrariesPaths()
		{
			DTE dte = (DTE)GetService(typeof(DTE));

			Projects projects = (Projects)dte.GetObject("VCProjects");
			VCProjectEngine projectEngine = (VCProjectEngine)
				projects.Properties.Item("VCProjectEngine").Object;
			IVCCollection platforms = (IVCCollection)projectEngine.Platforms;

			VCPlatform vcp = (VCPlatform)platforms.Item("Win32");

			string packageDir = Path.GetDirectoryName(
				Assembly.GetExecutingAssembly().Location);

			string cxxTestIncludePath = Path.Combine(
				packageDir, "Include\\cxxtest");
			string derefereeIncludePath = Path.Combine(
				packageDir, "Include\\dereferee");
			string supportLibPath = Path.Combine(packageDir, "Libraries");

			string newIncludes = AddPathsToDirectories(
				vcp.IncludeDirectories, new string[] {
					cxxTestIncludePath, derefereeIncludePath
				});
			vcp.IncludeDirectories = newIncludes;

			string newLibraries = AddPathsToDirectories(
				vcp.LibraryDirectories, new string[] {
					supportLibPath
				});
			vcp.LibraryDirectories = newLibraries;

			vcp.CommitChanges();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Takes a semicolon-separated path string and adds the specified
		/// array of paths to it, avoiding duplicates.
		/// </summary>
		/// <param name="dirString">
		/// The semicolon-separated path string.
		/// </param>
		/// <param name="paths">
		/// The array of paths to add to the string.
		/// </param>
		/// <returns>
		/// The new semicolon-separated path string.
		/// </returns>
		private string AddPathsToDirectories(string dirString, string[] paths)
		{
			List<string> dirs = new List<string>(dirString.Split(';'));

			foreach (string path in paths)
			{
				int index = dirs.FindIndex(new Predicate<string>(
					delegate(string element)
					{
						return Path.Equals(path, element);
					}));

				if (index == -1)
					dirs.Add(path);
			}

			return string.Join(";", dirs.ToArray());
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the IVsTrackProjectDocuments2 service from Visual Studio.
		/// </summary>
		/// <returns>
		/// The IVsTrackProjectDocuments2 interface reference.
		/// </returns>
		private IVsTrackProjectDocuments2 GetTrackProjectDocuments()
		{
			Microsoft.VisualStudio.OLE.Interop.IServiceProvider sp =
				(Microsoft.VisualStudio.OLE.Interop.IServiceProvider)
				GetService(typeof(DTE));

			Guid guidSP = typeof(SVsTrackProjectDocuments).GUID;
			Guid guidIID = typeof(IVsTrackProjectDocuments2).GUID;
			IntPtr ptrUnknown;
			sp.QueryService(ref guidSP, ref guidIID, out ptrUnknown);
			IVsTrackProjectDocuments2 vsTrackProjDocs =
				(IVsTrackProjectDocuments2)
				Marshal.GetObjectForIUnknown(ptrUnknown);
			Marshal.Release(ptrUnknown);

			return vsTrackProjDocs;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when a solution is opened in the IDE.
		/// </summary>
		private void SolutionEvents_Opened()
		{
			AdviseCodeModelEvents(CodeModel_ElementAdded,
				CodeModel_ElementChanged, CodeModel_ElementDeleted);

			// For some reason the code model isn't available immediately
			// when the solution is opened (perhaps it is populated in another
			// thread?), so we wait one second before attempting to refresh
			// the CxxTest Suites window. It's not a perfect solution, but
			// it seems to work for now.

			System.Threading.Timer timer = new System.Threading.Timer(
				delegate(object obj)
				{
					TryToRefreshTestSuitesWindow();
				},
				null, 1000, Timeout.Infinite);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when a project is added to the solution.
		/// </summary>
		/// <param name="project">
		/// The project that was added to the solution.
		/// </param>
		private void SolutionEvents_ProjectAdded(Project project)
		{
			TryToRefreshTestSuitesWindow();			
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when a project is removed from the solution.
		/// </summary>
		/// <param name="project">
		/// The project that was removed.
		/// </param>
		private void SolutionEvents_ProjectRemoved(Project project)
		{
			TryToRefreshTestSuitesWindow();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called before the solution is closed.
		/// </summary>
		private void SolutionEvents_BeforeClosing()
		{
			// Unhook ourselves from the code model event sink.

			UnadviseCodeModelEvents(CodeModel_ElementAdded,
				CodeModel_ElementChanged, CodeModel_ElementDeleted);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called after the solution is closed.
		/// </summary>
		private void SolutionEvents_AfterClosing()
		{
			TryToRefreshTestSuitesWindow();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when an element is added to the code model.
		/// </summary>
		/// <param name="element">
		/// The element that was added.
		/// </param>
		private void CodeModel_ElementAdded(CodeElement element)
		{
			// Disabled until this can be made more stable.

			// TryToAddElementToTestSuitesView(element);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when an element is changed in the code model.
		/// </summary>
		/// <param name="element">
		/// The element that was changed.
		/// </param>
		/// <param name="changeKind">
		/// The kind of change that occurred.
		/// </param>
		private void CodeModel_ElementChanged(CodeElement element,
			vsCMChangeKind changeKind)
		{
			// Disabled until can be made more stable.

			// TryToChangeElementToTestSuitesView(element, changeKind);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when an element is removed from the code model.
		/// </summary>
		/// <param name="parent">
		/// The parent of the element that was removed.
		/// </param>
		/// <param name="element">
		/// The element that was removed.
		/// </param>
		private void CodeModel_ElementDeleted(object parent,
			CodeElement element)
		{
			// Disabled until can be made more stable.

			// TryToDeleteElementToTestSuitesView(parent, element);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called to update the status of the "Generate New Suite" command
		/// before it is displayed.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		private void GenerateTests_BeforeQueryStatus(object sender,
			EventArgs e)
		{
			OleMenuCommand command = (OleMenuCommand)sender;

			HierarchyItem[] selection =
				VsShellUtils.GetCurrentSelection(this);

			// Don't support test generation if more than one file is
			// selected.

			if (selection.Length > 1)
			{
				command.Supported = false;
				return;
			}

			ProjectItem item = selection[0].GetExtObjectAs<ProjectItem>();
			if (item == null)
			{
				command.Supported = false;
				return;
			}

			VCFileCodeModel codeModel = (VCFileCodeModel)item.FileCodeModel;

			// Don't support test generation if the file contains unit tests
			// already.

			if (codeModel == null)
			{
				command.Supported = false;
				return;
			}
			else
			{
				foreach (VCCodeClass klass in codeModel.Classes)
				{
					if (klass.Bases.Count > 0 &&
						Constants.CxxTestSuiteClassRegex.IsMatch(
							klass.Bases.Item(1).Name))
					{
						command.Supported = false;
						return;
					}
				}
			}

			command.Supported = true;
		}


		// ------------------------------------------------------
		/// <summary>
        /// Called when the "Generate New Suite" command is invoked.
        /// </summary>
        /// <param name="sender">
		/// The sender of the event.
		/// </param>
        /// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
        private void GenerateTests_Invoked(object sender, EventArgs e)
        {
			HierarchyItem[] selection =
				VsShellUtils.GetCurrentSelection(this);
			
			HierarchyItem headerUnderTest = null;

			if (selection.Length > 0)
				headerUnderTest = selection[0];

			NewCxxTestSuiteWizard wizard =
				new NewCxxTestSuiteWizard(this, headerUnderTest);

			wizard.ShowDialog();
			return;
        }


		// ------------------------------------------------------
		/// <summary>
		/// Gets the path to the executable file generated by the active
		/// configuration of the specified project.
		/// </summary>
		/// <param name="projectHier">
		/// The project to get the executable file for.
		/// </param>
		/// <returns>
		/// The full path to the executable generated by the active
		/// configuration for this project.
		/// </returns>
		private string GetActiveExecutablePath(HierarchyItem projectHier)
		{
			Project project = projectHier.GetExtObjectAs<Project>();
			Configuration activeConfig =
				project.ConfigurationManager.ActiveConfiguration;

			string outputPath =
				activeConfig.Properties.Item("OutputPath").Value as string;
			string outputName = ((object[]) activeConfig.OutputGroups.Item(
				"Built").FileNames)[0] as string;

			return Path.Combine(outputPath, outputName);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the path to the working directory used by the active
		/// configuration of the specified project.
		/// </summary>
		/// <param name="projectHier">
		/// The project to get the working directory for.
		/// </param>
		/// <returns>
		/// The full path to the working directory used by the active
		/// configuration for this project.
		/// </returns>
		private string GetActiveWorkingDirectory(HierarchyItem projectHier)
		{
			Project project = projectHier.GetExtObjectAs<Project>();
			Configuration activeConfig =
				project.ConfigurationManager.ActiveConfiguration;

			Property workingDirProp =
				activeConfig.Properties.Item("WorkingDirectory");
			string workingDir = workingDirProp.Value as string;

			if (string.IsNullOrEmpty(workingDir))
				workingDir = projectHier.ProjectDirectory;

			return workingDir;
		}


		// ------------------------------------------------------
		/// <summary>
        /// Creates the test runner source file for the current startup
		/// project and adds it to the project.
        /// </summary>
		/// <param name="doNotRunTests">
		/// If true, the file will be generated but will not include any
		/// statements to run any tests. If false, the tests will be run
		/// based on the current selection in the CxxTest Suites view.
		/// </param>
        public void CreateTestRunnerFile(bool doNotRunTests)
        {
			TestSuiteCollection suites = AllTestSuites;
			
			Dictionary<string, bool> testsToRun;
			
			if(doNotRunTests)
				testsToRun = null;
			else
				testsToRun = TryToGetTestsToRun();

			HierarchyItem startupProject =
				VsShellUtils.GetStartupProject(this);

			string projectDir = startupProject.ProjectDirectory;
			string workingDir = GetActiveWorkingDirectory(startupProject);

			Project project = startupProject.GetExtObjectAs<Project>();

			string runnerPath = Path.Combine(
				projectDir, Constants.TestRunnerFilename);

			// Remove the file from the project if necessary, and delete it
			// from the file system if it exists.
			VCProject vcproj = (VCProject)project.Object;
			if (!vcproj.CanAddFile(runnerPath))
			{
				IVCCollection coll = (IVCCollection)vcproj.Files;
				vcproj.RemoveFile(coll.Item(Constants.TestRunnerFilename));
			}

			if(File.Exists(runnerPath))
				File.Delete(runnerPath);

			TestRunnerGenerator generator =
				new TestRunnerGenerator(runnerPath, suites, testsToRun);

			generator.Generate();

			// Add the file to the project.

			vcproj.AddFile(runnerPath);
        }


		// ------------------------------------------------------
		/// <summary>
        /// Builds the solution in preparation for a test launch.
        /// </summary>
        public void BuildSolutionToRunTests()
        {
			TryToSetTestResultsText("Building the solution...");

            solutionBuildEvents.BuildingForTestLaunch = true;

            IVsSolutionBuildManager man = (IVsSolutionBuildManager)
				GetService(typeof(SVsSolutionBuildManager));

			uint flags = (uint)VSSOLNBUILDUPDATEFLAGS.SBF_OPERATION_BUILD;

			uint queryResults = (uint)(
				VSSOLNBUILDQUERYRESULTS.VSSBQR_CONTDEPLOYONERROR_QUERY_NO |
				VSSOLNBUILDQUERYRESULTS.VSSBQR_OUTOFDATE_QUERY_YES |
				VSSOLNBUILDQUERYRESULTS.VSSBQR_SAVEBEFOREBUILD_QUERY_YES);

			int result = man.StartSimpleUpdateSolutionConfiguration(
				flags, queryResults, 1);
        }


		// ------------------------------------------------------
		/// <summary>
        /// Launches the tests for the solution.
        /// </summary>
        public void LaunchTests()
        {
			HierarchyItem startupProject =
				VsShellUtils.GetStartupProject(this);
			lastRunProject = startupProject;

			if (startupProject == null)
				return;

			TryToSetTestResultsText("Running the tests...");

			debuggerEvents.StartingTestRun = true;

			uint flags =
				(uint)__VSDBGLAUNCHFLAGS.DBGLAUNCH_StopDebuggingOnEnd;

			string projectDir = startupProject.ProjectDirectory;
			string workingDir = GetActiveWorkingDirectory(startupProject);
			string exePath = GetActiveExecutablePath(startupProject);

			Guid guidNativeOnlyEng =
				new Guid("3B476D35-A401-11D2-AAD4-00C04F990171");

			VsDebugTargetInfo debugTarget = new VsDebugTargetInfo();
			debugTarget.bstrExe = exePath;
			debugTarget.bstrCurDir = workingDir;
			debugTarget.fSendStdoutToOutputWindow = 1;
			debugTarget.grfLaunch = flags;
			debugTarget.dlo = DEBUG_LAUNCH_OPERATION.DLO_CreateProcess;
			debugTarget.clsidCustom = guidNativeOnlyEng;
			VsShellUtilities.LaunchDebugger(this, debugTarget);
        }


		// ------------------------------------------------------
		/// <summary>
        /// Opens a file in the IDE and jumps to the specified line, selecting
		/// it in the text editor.
        /// </summary>
        /// <param name="document">
		/// The full path to the file that should be opened.
		/// </param>
        /// <param name="line">
		/// The line number to jump to.
		/// </param>
        public void OpenFileInEditor(string document, int line)
        {
			string title = Path.GetFileName(document);
            DTE dte = (DTE)GetService(typeof(SApplicationObject));

			dte.ExecuteCommand("File.OpenFile", "\"" + document + "\"");
            dte.Documents.Item(title).Activate();
            dte.Application.ExecuteCommand("GotoLn", line.ToString());
            dte.Application.ExecuteCommand("Edit.LineEndExtend", "");
        }


		//~ Static/instance variables ........................................

		// The sole instance of the CxxTestPackage class.
		private static CxxTestPackage instance = null;

		// IDE event handler objects.
		private SolutionBuildEventsHandler solutionBuildEvents;
		private DebuggerEventsHandler debuggerEvents;
		private TrackProjectDocumentsEventsHandler trackProjectDocsEvents;

		// Cookies for the above event handlers.
		private uint solutionBuildCookie;
		private uint debuggerCookie;
		private uint trackProjectDocsCookie;

		// The hierarchy item that represents the project that was last run
		// (using "Run Selected Tests") by the user.
		private HierarchyItem lastRunProject;
    }
}