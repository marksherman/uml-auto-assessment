/*==========================================================================*\
 |  $Id: SubmitterOptionsPanel.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using Microsoft.Win32;
using System.Runtime.InteropServices;
using WebCAT.Submitter.VisualStudio.Utility;

namespace WebCAT.Submitter.VisualStudio.Forms
{
	/// <summary>
	/// A panel that is displayed in the Tools/Options dialog in Visual
	/// Studio to allow the user to modify the preferences used by the
	/// submitter package.
	/// </summary>
	internal partial class SubmitterOptionsPanel : UserControl
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the submitter options panel.
		/// </summary>
		public SubmitterOptionsPanel()
		{
			InitializeComponent();
		}

		public void Initialize(SubmitterOptionsPage controller)
		{
			pageController = controller;

			submissionDefsUrlField.Text =
				pageController.SubmissionDefinitionsUri;
			defaultUsernameField.Text = pageController.DefaultUsername;
			mailServerField.Text = pageController.OutgoingMailServer;
			emailAddressField.Text = pageController.EmailAddress;
		}

		private void submissionDefsUrlField_TextChanged(
			object sender, EventArgs e)
		{
			pageController.SubmissionDefinitionsUri =
				submissionDefsUrlField.Text;
		}

		private void defaultUsernameField_TextChanged(
			object sender, EventArgs e)
		{
			pageController.DefaultUsername = defaultUsernameField.Text;
		}

		private void mailServerField_TextChanged(
			object sender, EventArgs e)
		{
			pageController.OutgoingMailServer = mailServerField.Text;
		}

		private void emailAddressField_TextChanged(
			object sender, EventArgs e)
		{
			pageController.EmailAddress = emailAddressField.Text;
		}

		private SubmitterOptionsPage pageController;
	}
}
