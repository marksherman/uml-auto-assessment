/*==========================================================================*\
 |  $Id: NewCxxTestSuiteWizardPage1.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using WebCAT.CxxTest.VisualStudio.Utility;
using Microsoft.VisualStudio;
using System.IO;
using Microsoft.VisualStudio.VCProjectEngine;
using EnvDTE;
using Microsoft.VisualStudio.Shell;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.VCProject;
using stdole;
using System.Runtime.InteropServices;
using Microsoft.Win32;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	public partial class NewCxxTestSuiteWizardPage1 : WizardPageControl
	{
		internal NewCxxTestSuiteWizardPage1(IServiceProvider sp, HierarchyItem headerUnderTest)
		{
			InitializeComponent();

			serviceProvider = sp;
			this.headerUnderTest = headerUnderTest;

			// Initialize the project list.

			foreach (HierarchyItem item in VsShellUtils.GetLoadedProjects(serviceProvider))
			{
				existingProjectsCombo.Items.Add(new ProjectItem(item));
			}

			if (headerUnderTest != null)
			{
				string header = headerUnderTest.ProjectRelativePath;
				headerUnderTestField.Text = header;
				
				nameField.Text = Path.GetFileNameWithoutExtension(header) + "Tests";

				bool madeSelection = false;

				foreach (ProjectItem item in existingProjectsCombo.Items)
				{
					if (item.Item.Equals(headerUnderTest.ContainingProject))
					{
						existingProjectsCombo.SelectedItem = item;
						madeSelection = true;
						break;
					}
				}

				if (!madeSelection)
				{
					existingProjectsCombo.SelectedIndex = 0;
				}
			}

			superclassField.Text = "CxxTest::TestSuite";
		}

		internal HierarchyItem SelectedProject
		{
			get
			{
				return ((ProjectItem)existingProjectsCombo.SelectedItem).Item;
			}
		}

		public string SuiteName
		{
			get
			{
				return nameField.Text;
			}
		}

		internal HierarchyItem HeaderUnderTest
		{
			get
			{
				return headerUnderTest;
			}
		}

		public string Superclass
		{
			get
			{
				return superclassField.Text;
			}
		}

		public bool CreateSetUp
		{
			get
			{
				return setUpButton.Checked;
			}
		}

		public bool CreateTearDown
		{
			get
			{
				return tearDownButton.Checked;
			}
		}

		private void headerUnderTestBrowseButton_Click(object sender, EventArgs e)
		{
			HierarchyItemDialog dialog = new HierarchyItemDialog(serviceProvider);
			dialog.Message = "Choose a header file in your project to generate a test suite from.";
			dialog.SelectableTypeGuids = new Guid[] { VSConstants.GUID_ItemType_PhysicalFile };

			if (dialog.ShowDialog() == DialogResult.OK)
			{
				HierarchyItem item = dialog.SelectedItem;
				this.headerUnderTest = item;

				headerUnderTestField.Text = item.ProjectRelativePath;
			}
		}

		private class ProjectItem
		{
			public ProjectItem(HierarchyItem item)
			{
				this.item = item;
			}

			public HierarchyItem Item
			{
				get
				{
					return item;
				}
			}

			public override string ToString()
			{
				return item.Name;
			}

			private HierarchyItem item;
		}

		public override void OnGoingToNextPage(WizardPageControl nextPage)
		{
			NewCxxTestSuiteWizardPage2 page2 = (NewCxxTestSuiteWizardPage2)nextPage;

			page2.PopulateWithHeader(headerUnderTest);
		}

		private IServiceProvider serviceProvider;
		private HierarchyItem headerUnderTest;
	}
}
