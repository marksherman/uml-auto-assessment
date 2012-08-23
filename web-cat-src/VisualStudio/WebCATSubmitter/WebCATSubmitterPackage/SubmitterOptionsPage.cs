/*==========================================================================*\
 |  $Id: SubmitterOptionsPage.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using Microsoft.VisualStudio.Shell;
using System.Runtime.InteropServices;
using System.ComponentModel;
using WebCAT.Submitter.VisualStudio.Forms;
using System.Windows.Forms;

namespace WebCAT.Submitter.VisualStudio
{
	/// <summary>
	/// Acts as a controller for reading and writing the package's options to
	/// permanent storage (the registry).
	/// </summary>
	[ClassInterface(ClassInterfaceType.AutoDual),
	 Guid("BF7C5E83-5677-4b29-AAFB-5012109BAD28")]
	internal class SubmitterOptionsPage : DialogPage
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates and initializes the user control that represents this
		/// Tools/Options page.
		/// </summary>
		[Browsable(false)]
		[DesignerSerializationVisibility(
			DesignerSerializationVisibility.Hidden)]
		protected override IWin32Window Window
		{
			get
			{
				SubmitterOptionsPanel panel = new SubmitterOptionsPanel();
				panel.Initialize(this);
				return panel;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the URI from which the submission targets definitions
		/// should be loaded.
		/// </summary>
		public string SubmissionDefinitionsUri
		{
			get
			{
				return submissionDefsUri;
			}
			set
			{
				submissionDefsUri = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the default username that will be initially entered in
		/// the submission wizard when it is opened.
		/// </summary>
		public string DefaultUsername
		{
			get
			{
				return defaultUsername;
			}
			set
			{
				defaultUsername = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the outgoing (SMTP) mail server to use when making
		/// mailto: submissions.
		/// </summary>
		public string OutgoingMailServer
		{
			get
			{
				return outgoingMailServer;
			}
			set
			{
				outgoingMailServer = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the user's e-mail address that will be used in
		/// mailto: submissions.
		/// </summary>
		public string EmailAddress
		{
			get
			{
				return emailAddress;
			}
			set
			{
				emailAddress = value;
			}
		}


		// ==== Fields ========================================================

		// The URI of the submission targets definitions.
		private string submissionDefsUri;

		// The default user name to put into the wizard.
		private string defaultUsername;

		// The outgoing mail server to use in mailto: submissions.
		private string outgoingMailServer;

		// The user's e-mail address to use in mailto: submissions.
		private string emailAddress;
	}
}
