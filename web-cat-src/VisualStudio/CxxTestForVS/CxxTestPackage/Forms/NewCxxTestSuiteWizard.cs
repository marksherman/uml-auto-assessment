/*==========================================================================*\
 |  $Id: NewCxxTestSuiteWizard.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
using WebCAT.CxxTest.VisualStudio.Utility;
using Microsoft.VisualStudio.VCCodeModel;
using EnvDTE;
using Microsoft.VisualStudio.Shell.Interop;
using WebCAT.CxxTest.VisualStudio.Templating;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	// --------------------------------------------------------------------
	/// <summary>
	/// A wizard that assists the user in creating a new test suite based on a
	/// header file in their solution.
	/// </summary>
	internal class NewCxxTestSuiteWizard : WizardForm
	{
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new CxxTest suite generating wizard.
		/// </summary>
		/// <param name="sp">
		/// The service provider to use to access Visual Studio services.
		/// </param>
		/// <param name="headerUnderTest">
		/// The hierarchy item representing the header that the test suite
		/// should be generated from.
		/// </param>
		public NewCxxTestSuiteWizard(IServiceProvider sp,
			HierarchyItem headerUnderTest)
		{
			serviceProvider = sp;

			// Create the wizard pages and add them to the wizard.

			page1 = new NewCxxTestSuiteWizardPage1(sp, headerUnderTest);
			page2 = new NewCxxTestSuiteWizardPage2(sp);

			AddPage(page1);
			AddPage(page2);

			this.BannerTitle = "New CxxTest Suite";
			this.BannerMessage = "Select the name of the new CxxTest suite. " +
				"You have the options to specify the header of the class " +
				"under test and on the next page, the methods to be tested.";
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Called when the Finish button is clicked in the wizard.
		/// </summary>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		protected override void OnFinish(EventArgs e)
		{
			base.OnFinish(e);

			HierarchyItem projectItem = page1.SelectedProject;
			string suiteName = page1.SuiteName;
			HierarchyItem headerUnderTest = page1.HeaderUnderTest;
			string superclass = page1.Superclass;
			bool createSetUp = page1.CreateSetUp;
			bool createTearDown = page1.CreateTearDown;
			VCCodeFunction[] functions = page2.SelectedFunctions;

			// Invoke the test suite generator to create the file and add it
			// to the project.

			TestSuiteGenerator generator = new TestSuiteGenerator(
				projectItem, suiteName, headerUnderTest, superclass,
				createSetUp, createTearDown, functions);

			generator.Generate();

			// Open the new test suite in the IDE.

			HierarchyItem suiteFile = generator.GeneratedSuiteFile;

			IVsProject vsproj = projectItem.Hierarchy as IVsProject;
			Guid guidNull = Guid.Empty;
			IVsWindowFrame windowFrame;
			vsproj.OpenItem(suiteFile.ItemID, ref guidNull, IntPtr.Zero, out windowFrame);
		}


		//~ Instance variables ...............................................

		// The service provider used to access Visual Studio services.
		private IServiceProvider serviceProvider;

		// The wizard pages.
		private NewCxxTestSuiteWizardPage1 page1;
		private NewCxxTestSuiteWizardPage2 page2;
	}
}
