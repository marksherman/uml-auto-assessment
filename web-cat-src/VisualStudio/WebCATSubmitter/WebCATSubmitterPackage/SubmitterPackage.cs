/*==========================================================================*\
 |  $Id: SubmitterPackage.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.Diagnostics;
using System.Globalization;
using System.Runtime.InteropServices;
using System.ComponentModel.Design;
using Microsoft.Win32;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.OLE.Interop;
using Microsoft.VisualStudio.Shell;
using WebCAT.Submitter.VisualStudio.Utility;
using System.Collections.Generic;
using System.IO;
using WebCAT.Submitter.VisualStudio.Submittables;
using WebCAT.Submitter.VisualStudio.Forms;
using System.ComponentModel;
using System.Windows.Forms;

namespace WebCAT.Submitter.VisualStudio
{
    /// <summary>
    /// This is the class that implements the package exposed by this assembly.
    ///
    /// The minimum requirement for a class to be considered a valid package for Visual Studio
    /// is to implement the IVsPackage interface and register itself with the shell.
    /// This package uses the helper classes defined inside the Managed Package Framework (MPF)
    /// to do it: it derives from the Package class that provides the implementation of the 
    /// IVsPackage interface and uses the registration attributes defined in the framework to 
    /// register itself and its components with the shell.
    /// </summary>
    [PackageRegistration(UseManagedResourcesOnly = true)]
    [DefaultRegistryRoot("Software\\Microsoft\\VisualStudio\\8.0")]
    [InstalledProductRegistration(false, "#110", "#112", "1.0",
		IconResourceID = 400)]
    [ProvideLoadKey("Standard", "1.0", "Web-CAT Electronic Submitter",
		"Web-CAT Development Team", 1)]
    [ProvideMenuResource(1000, 1)]
	[ProvideProfileAttribute(typeof(SubmitterOptionsPage), "Web-CAT",
		"Electronic Submissions", 106, 107, true,
		DescriptionResourceID = 108)]
	[ProvideOptionPage(typeof(SubmitterOptionsPage), "Web-CAT",
		"Electronic Submissions", 0, 0, true)]
	[Guid(Guids.WebCATSubmitterPackageString)]
    public sealed class SubmitterPackage : Package
    {
		//  -------------------------------------------------------------------
        /// <summary>
        /// Performs any initialization that does not require that the package
		/// yet be sited inside the Visual Studio environment.
        /// </summary>
        public SubmitterPackage()
        {
			// Reserve the filename for the temp file that will be used to
			// store submission server responses so that they can be loaded
			// into a browser.

			string tempPath = Path.GetTempFileName();
			responseTempPath = tempPath + ".htm";
			File.Move(tempPath, responseTempPath);
		}

		//  -------------------------------------------------------------------
        /// <summary>
		/// Performs further initialization after the package is sited inside
		/// the Visual Studio environment.
        /// </summary>
        protected override void Initialize()
        {
            base.Initialize();

            OleMenuCommandService mcs = GetService(typeof(IMenuCommandService))
				as OleMenuCommandService;

            if(null != mcs)
            {
				CommandID menuCommandID;

				menuCommandID = new CommandID(
					Guids.WebCATSubmitterPackageCmdSet,
					(int)CommandIds.SubmitSolution);
				mcs.AddCommand(new MenuCommand(
					new EventHandler(SubmitSolution_Invoked), menuCommandID));

				menuCommandID = new CommandID(
					Guids.WebCATSubmitterPackageCmdSet,
					(int)CommandIds.SubmitProject);
				mcs.AddCommand(new MenuCommand(
					new EventHandler(SubmitProject_Invoked), menuCommandID));
			}
        }

		//  -------------------------------------------------------------------
		/// <summary>
		/// Disposes of the package.
		/// </summary>
		/// <param name="disposing">
		/// True if disposing; false if finalizing.
		/// </param>
		protected override void Dispose(bool disposing)
		{
			if (disposing)
			{
				File.Delete(responseTempPath);
			}

			base.Dispose(disposing);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Collects the projects that are currently selected in the Solution
		/// Explorer and invokes the submission wizard.
		/// </summary>
		private void SubmitProject_Invoked(object sender, EventArgs e)
		{
			HierarchyItem[] selectedItems =
				VsShellUtils.GetCurrentSelection(this);

			List<ISubmittableItem> submittables = new List<ISubmittableItem>();

			IVsSolution solution =
				VsShellUtils.GetSolution(this);

			string solutionDir, solutionFile, solutionUser;
			solution.GetSolutionInfo(out solutionDir, out solutionFile,
				out solutionUser);

			if (selectedItems.Length > 0)
			{
				foreach (HierarchyItem selectedItem in selectedItems)
				{
					if (selectedItem.IsProject)
					{
						submittables.Add(new SubmittableProject(solutionDir,
							selectedItem));
					}
				}

				// Handle subprojects here by filtering them out of the list
				// if their parent project is also in the list?

				// If the selection includes all of the projects in the
				// solution, then just make the solution itself the current
				// selection.

				IEnumerable<HierarchyItem> allProjects =
					VsShellUtils.GetLoadedProjects(solution);

				if (AreAllProjectsInSubmittables(allProjects, submittables))
				{
					submittables.Clear();
					submittables.Add(new SubmittableSolution(solution));
				}

				if (submittables.Count > 0)
				{
					ShowSubmissionWizard(submittables.ToArray());
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether all of the projects in the given
		/// list of hierarchy items are also in the given list of submittable
		/// items.
		/// </summary>
		/// <remarks>
		/// This method is used to determine if we should just submit the whole
		/// solution when every project is selected.
		/// </remarks>
		/// <param name="allProjects">
		/// A list of hierarchy items that represent projects.
		/// </param>
		/// <param name="submittables">
		/// A list of submittable items.
		/// </param>
		/// <returns>
		/// True if all of the projects are contained in the list of
		/// submittable items.
		/// </returns>
		private bool AreAllProjectsInSubmittables(
			IEnumerable<HierarchyItem> allProjects,
			IEnumerable<ISubmittableItem> submittables)
		{
			List<HierarchyItem> masterList = new List<HierarchyItem>(
				allProjects);

			foreach (ISubmittableItem item in submittables)
			{
				SubmittableProject sp = item as SubmittableProject;

				if (sp != null)
				{
					masterList.Remove(sp.HierarchyItem);
				}
			}

			return (masterList.Count == 0);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Collects the current solution loaded into Visual Studio and invokes
		/// the submission wizard.
		/// </summary>
		private void SubmitSolution_Invoked(object sender, EventArgs e)
		{
			IVsSolution solution = VsShellUtils.GetSolution(this);

			if (solution != null)
			{
				ISubmittableItem[] items = {
					new SubmittableSolution(solution)
				};

				ShowSubmissionWizard(items);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Displays the submission wizard with the specified array of
		/// submittable items as its initial selection.
		/// </summary>
		/// <param name="items">
		/// The initial selection of submittable items.
		/// </param>
		private void ShowSubmissionWizard(ISubmittableItem[] items)
		{
			IntPtr owningHwnd;
			IVsUIShell uiShell = (IVsUIShell)GetService(typeof(SVsUIShell));
			uiShell.GetDialogOwnerHwnd(out owningHwnd);
			HWndWrapper windowWrapper = new HWndWrapper(owningHwnd);

			SubmissionEngine engine = new SubmissionEngine();

			SubmitterOptionsPage options = (SubmitterOptionsPage)
				GetDialogPage(typeof(SubmitterOptionsPage));

			Exception openException = null;

			BackgroundWorkerDialog dlg = new BackgroundWorkerDialog(
				Messages.LoadingSubmissionTargetsProgress, true,
				delegate(object sender, DoWorkEventArgs e)
				{
					BackgroundWorker worker = (BackgroundWorker)sender;

					try
					{
						Uri uri = new Uri(options.SubmissionDefinitionsUri);
						engine.OpenDefinitions(uri);
					}
					catch (Exception ex)
					{
						openException = ex;
					}
				});

			dlg.ShowDialog(windowWrapper);

			if (engine.Root != null)
			{
				SubmitterWizard wizard = new SubmitterWizard(engine,
					this, items, options.DefaultUsername);

				wizard.ShowDialog(windowWrapper);

				if (engine.HasResponse)
				{
					DisplaySubmissionResponse(engine);
				}
			}
			else
			{
				if (openException != null)
				{
					MessageBox.Show(windowWrapper,
						String.Format(Messages.LoadingSubmissionTargetsError,
						openException.Message),
						Messages.LoadingSubmissionTargetsErrorTitle,
						MessageBoxButtons.OK);
				}
				else
				{
					MessageBox.Show(windowWrapper,
						Messages.LoadingSubmissionTargetsUnknownError,
						Messages.LoadingSubmissionTargetsErrorTitle,
						MessageBoxButtons.OK);
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Displays the response that was generated by a submission.
		/// </summary>
		/// <remarks>
		/// The response is displayed by writing it to the temporary response
		/// HTML file and then opening the user's default browser.
		/// </remarks>
		/// <param name="engine">
		/// The submission engine from which to obtain the response.
		/// </param>
		private void DisplaySubmissionResponse(SubmissionEngine engine)
		{
			StreamWriter writer = new StreamWriter(responseTempPath, false);
			writer.Write(engine.SubmissionResponse);
			writer.Close();

			System.Diagnostics.Process.Start(responseTempPath);
		}


		// ==== Fields ========================================================

		// The absolute path to the temporary HTML file that will hold the
		// submission response.
		private string responseTempPath;
	}
}
