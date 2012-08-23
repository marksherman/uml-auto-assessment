/*==========================================================================*\
 |  $Id: SubmitterWizard.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using WebCAT.Submitter;
using WebCAT.Submitter.VisualStudio.Utility;
using WebCAT.Submitter.VisualStudio.Submittables;
using System.IO;

namespace WebCAT.Submitter.VisualStudio.Forms
{
	/// <summary>
	/// Implements a wizard that facilitates the submission of projects in
	/// Visual Studio.
	/// </summary>
	public partial class SubmitterWizard : Form
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the submission wizard.
		/// </summary>
		/// <param name="engine">
		/// The submission engine instance to use to make the submission.
		/// </param>
		/// <param name="dte">
		/// The DTE object to use to access Visual Studio services.
		/// </param>
		/// <param name="submittables">
		/// The array of items to be submitted.
		/// </param>
		public SubmitterWizard(SubmissionEngine engine, IServiceProvider sp,
			ISubmittableItem[] submittables, string username)
		{
			InitializeComponent();

			this.serviceProvider = sp;
			this.engine = engine;
			this.submittables = submittables;

			Controls.Remove(summaryPage);

			usernameField.Text = username;
			wizardDescriptionLabel.Text = Messages.WizardStartDescription;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Used to update the availability of the Next button based on whether
		/// the current selections and field values are valid.
		/// </summary>
		private void UpdateNextEnablement()
		{
			bool enabled = true;

			TargetTreeNode selNode = submissionTargetsTree.SelectedNode
				as TargetTreeNode;

			if (selNode == null || !selNode.Target.IsActionable)
				enabled = false;
			else if (usernameField.Text.Length == 0)
				enabled = false;
			else if (submittables.Length == 0)
				enabled = false;

			nextButton.Enabled = enabled;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Expands all of the local (non-imported) assignment groups in the
		/// tree view. Delay-loaded groups are not automatically expanded since
		/// they potentially require slower network access.
		/// </summary>
		/// <param name="parent">
		/// The node that should be expanded.
		/// </param>
		private void ExpandLocalGroups(TargetTreeNode parent)
		{
			if (parent.Target.IsLoaded)
			{
				parent.PopulateChildren(false);
				parent.Expand();

				foreach (TreeNode child in parent.Nodes)
				{
					TargetTreeNode targetChild = child as TargetTreeNode;

					if (targetChild != null)
					{
						ExpandLocalGroups(targetChild);
					}
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Performs the actual submission of the projects.
		/// </summary>
		private void DoSubmission()
		{
			TargetTreeNode node =
				(TargetTreeNode)submissionTargetsTree.SelectedNode;

			SubmissionManifest parameters = new SubmissionManifest();
			parameters.Assignment = (ITargetAssignment)node.Target;
			parameters.Username = usernameField.Text;
			parameters.Password = passwordField.Text;
			parameters.SubmittableItems = submittables;

			Exception exception = null;

			BackgroundWorkerDialog dialog = new BackgroundWorkerDialog(
				Messages.SendingSubmission, true,
				delegate(object sender, DoWorkEventArgs e)
				{
					try
					{
						engine.SubmitProject(parameters);
					}
					catch(Exception ex)
					{
						exception = ex;
					}
				});

			dialog.ShowDialog(this);

			if(exception == null)
			{
				string message = engine.HasResponse ?
					Messages.ResponseClose : Messages.NoResponseClose;

				SetSubmissionResults(SubmissionResultCode.Success, message);
			}
			else if (exception is RequiredFilesMissingException)
			{
				RequiredFilesMissingException e =
					(RequiredFilesMissingException)exception;

				StringBuilder builder = new StringBuilder();
				builder.Append(Messages.WizardMissingFilesPreamble);

				foreach (string file in e.MissingFiles)
				{
					builder.Append(String.Format(
						Messages.WizardMissingFilesListItem, file));
				}

				SetSubmissionResults(SubmissionResultCode.Incomplete,
					builder.ToString());
			}
			else if (exception is ProtocolNotRegisteredException)
			{
				ProtocolNotRegisteredException e =
					(ProtocolNotRegisteredException)exception;

				SetSubmissionResults(SubmissionResultCode.Error,
					String.Format(Messages.ProtocolNotFound, e.Scheme));
			}
			else
			{
				SetSubmissionResults(SubmissionResultCode.Error,
					exception.Message);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Updates the wizard summary page with the results of the submission.
		/// </summary>
		/// <param name="code">
		/// The status of the submission.
		/// </param>
		/// <param name="message">
		/// A message describing any details about the submission result.
		/// </param>
		private void SetSubmissionResults(SubmissionResultCode code,
			string message)
		{
			Bitmap image = null;
			string summary = null;

			switch (code)
			{
				case SubmissionResultCode.Success:
					image = SystemIcons.Information.ToBitmap();
					summary = Messages.WizardSubmissionSuccessful;
					break;

				case SubmissionResultCode.Incomplete:
					image = SystemIcons.Warning.ToBitmap();
					summary = Messages.WizardMissingFilesMessage;
					break;

				case SubmissionResultCode.Error:
					image = SystemIcons.Error.ToBitmap();
					summary = Messages.WizardSubmissionFailed;
					break;
			}

			summaryImageLabel.Image = image;
			summaryLabel.Text = summary;
			summaryDetailsField.Text = message;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Updates the read-only text field that displays a string
		/// representation of the selected projects to be submitted.
		/// </summary>
		private void UpdateSubmittablesField()
		{
			if (submittables.Length == 0)
			{
				projectsToSubmitField.Text = Messages.ChooseProjectsPrompt;
			}
			else if (submittables[0] is SubmittableSolution)
			{
				SubmittableSolution ss = (SubmittableSolution)submittables[0];
				projectsToSubmitField.Text =
					String.Format(Messages.EntireSolutionMessage,
					Path.GetFileNameWithoutExtension(ss.SolutionName));
			}
			else
			{
				SubmittableProject sp = (SubmittableProject)submittables[0];
				StringBuilder builder = new StringBuilder();
				builder.Append(sp.HierarchyItem["Name"]);

				for (int i = 1; i < submittables.Length; i++)
				{
					builder.Append(", ");
					builder.Append(((SubmittableProject)submittables[i]).
						HierarchyItem["Name"]);
				}

				projectsToSubmitField.Text = builder.ToString();
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the form is loaded to initialize various UI components.
		/// </summary>
		/// <param name="sender">
		/// The form being loaded.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void SubmitterWizard_Load(object sender, EventArgs e)
		{
			// Add submission targets to the tree view.

			foreach (ITarget target in engine.Root.Children)
			{
				TargetTreeNode node = new TargetTreeNode(target);
				submissionTargetsTree.Nodes.Add(node);
				ExpandLocalGroups(node);
			}

			UpdateSubmittablesField();
			UpdateNextEnablement();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called before a tree node is about to be expanded so that its
		/// children can be delay-loaded.
		/// </summary>
		/// <param name="sender">
		/// The tree view whose node is being expanded.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void submissionTargetsTree_BeforeExpand(object sender,
			TreeViewCancelEventArgs e)
		{
			if (e.Node is TargetTreeNode)
			{
				((TargetTreeNode)e.Node).PopulateChildren(true);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called after the selection changes in the tree view.
		/// </summary>
		/// <param name="sender">
		/// The tree view whose selection has changed.
		/// </param>
		/// <param name="e">
		/// The event arguments describing the event.
		/// </param>
		private void submissionTargetsTree_AfterSelect(object sender,
			TreeViewEventArgs e)
		{
			UpdateNextEnablement();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the Next button is clicked to move on to the next
		/// page of the wizard.
		/// </summary>
		/// <param name="sender">
		/// The button that was clicked.
		/// </param>
		/// <param name="e">
		/// The event arguments that describe the event.
		/// </param>
		private void nextButton_Click(object sender, EventArgs e)
		{
			DoSubmission();

			Controls.Remove(startPage);
			Controls.Add(summaryPage);

			wizardDescriptionLabel.Text = Messages.WizardSummaryDescription;

			nextButton.Enabled = false;
			cancelButton.Text = Messages.CloseButtonText;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the Choose... button is clicked to choose the projects
		/// to be submitted.
		/// </summary>
		/// <param name="sender">
		/// The button that was clicked.
		/// </param>
		/// <param name="e">
		/// The event arguments that describe the event.
		/// </param>
		private void chooseProjectsToSubmit_Click(object sender, EventArgs e)
		{
			ChooseProjectsToSubmitDialog dialog =
				new ChooseProjectsToSubmitDialog(serviceProvider, submittables);

			if (dialog.ShowDialog(this) == DialogResult.OK)
			{
				submittables = dialog.Selection;
				UpdateSubmittablesField();
				UpdateNextEnablement();
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when the username text field is updated.
		/// </summary>
		/// <param name="sender">
		/// The text field that was changed.
		/// </param>
		/// <param name="e">
		/// The event arguments that describe the event.
		/// </param>
		private void usernameField_TextChanged(object sender, EventArgs e)
		{
			UpdateNextEnablement();
		}


		//  ==== Fields =======================================================

		// The submission engine to use.
		private SubmissionEngine engine;

		// The DTE object to use to access Visual Studio services.
		private IServiceProvider serviceProvider;

		// The array of items that will be submitted.
		private ISubmittableItem[] submittables;


		//  ==== Private Types ================================================

		/// <summary>
		/// Values used to describe the success or failure of a submission in
		/// the user interface.
		/// </summary>
		private enum SubmissionResultCode
		{
			Success,
			Incomplete,
			Error
		}
	}
}