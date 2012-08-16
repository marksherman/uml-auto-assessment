/*==========================================================================*\
 |  $Id: BackgroundWorkerDialog.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace WebCAT.Submitter.VisualStudio.Forms
{
	/// <summary>
	/// A dialog that manages the lifetime of a progress notifying job,
	/// starting the job when it is displayed and closing the dialog when the
	/// job is complete.
	/// </summary>
	public partial class BackgroundWorkerDialog : Form
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the background worker dialog.
		/// </summary>
		/// <param name="description">
		/// The description of the job to be displayed in the dialog.
		/// </param>
		/// <param name="marquee">
		/// True if the progress bar in the dialog should be a marquee
		/// (indeterminate progress) instead of a percentage.
		/// </param>
		/// <param name="workHandler">
		/// The work handler that should be executed to start the job.
		/// </param>
		public BackgroundWorkerDialog(string description,
			bool marquee, DoWorkEventHandler workHandler)
		{
			InitializeComponent();

			descriptionLabel.Text = description;

			if (marquee)
				progressBar.Style = ProgressBarStyle.Marquee;

			backgroundWorker.DoWork += workHandler;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the dialog is displayed. This override causes the job
		/// to begin when the dialog is shown.
		/// </summary>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		protected override void OnShown(EventArgs e)
		{
			base.OnShown(e);

			backgroundWorker.RunWorkerAsync();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the progress of the job is updated.
		/// </summary>
		/// <param name="sender">
		/// The background worker that sent the event.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void backgroundWorker_ProgressChanged(object sender,
			ProgressChangedEventArgs e)
		{
			progressBar.Value = e.ProgressPercentage;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the job has completed to close the dialog.
		/// </summary>
		/// <param name="sender">
		/// The background worker that sent the event.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void backgroundWorker_RunWorkerCompleted(object sender,
			RunWorkerCompletedEventArgs e)
		{
			if (e.Cancelled)
				this.DialogResult = DialogResult.Cancel;
			else
				this.DialogResult = DialogResult.OK;

			Close();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the Cancel button on the dialog is clicked.
		/// </summary>
		/// <param name="sender">
		/// The button that sent the event.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void cancelButton_Click(object sender, EventArgs e)
		{
			backgroundWorker.CancelAsync();
			cancelButton.Enabled = false;
		}
	}
}
