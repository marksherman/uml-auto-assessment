/*==========================================================================*\
 |  $Id: SubmitterWizard.designer.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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

namespace WebCAT.Submitter.VisualStudio.Forms
{
	partial class SubmitterWizard
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SubmitterWizard));
			this.projectsToSubmitLabel = new System.Windows.Forms.Label();
			this.submissionTargetsTree = new System.Windows.Forms.TreeView();
			this.usernameField = new System.Windows.Forms.TextBox();
			this.passwordField = new System.Windows.Forms.TextBox();
			this.cancelButton = new System.Windows.Forms.Button();
			this.nextButton = new System.Windows.Forms.Button();
			this.chooseProjectsToSubmit = new System.Windows.Forms.Button();
			this.projectsToSubmitField = new System.Windows.Forms.TextBox();
			this.passwordLabel = new System.Windows.Forms.Label();
			this.usernameLabel = new System.Windows.Forms.Label();
			this.assignmentsLabel = new System.Windows.Forms.Label();
			this.sepStartPage2 = new System.Windows.Forms.Label();
			this.sepStartPage1 = new System.Windows.Forms.Label();
			this.sepWizardBottom = new System.Windows.Forms.Label();
			this.wizardTitlePanel = new System.Windows.Forms.Panel();
			this.wizardDescriptionLabel = new System.Windows.Forms.Label();
			this.wizardTitleLabel = new System.Windows.Forms.Label();
			this.sepWizardTop = new System.Windows.Forms.Label();
			this.summaryPage = new System.Windows.Forms.Panel();
			this.summaryDetailsLabel = new System.Windows.Forms.Label();
			this.summaryDetailsField = new System.Windows.Forms.TextBox();
			this.summaryLabel = new System.Windows.Forms.Label();
			this.summaryImageLabel = new System.Windows.Forms.Label();
			this.startPage = new System.Windows.Forms.TableLayoutPanel();
			this.projectsToSubmitLayoutPanel = new System.Windows.Forms.TableLayoutPanel();
			this.wizardButtonsLayoutPanel = new System.Windows.Forms.TableLayoutPanel();
			this.wizardTitlePanel.SuspendLayout();
			this.summaryPage.SuspendLayout();
			this.startPage.SuspendLayout();
			this.projectsToSubmitLayoutPanel.SuspendLayout();
			this.wizardButtonsLayoutPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// projectsToSubmitLabel
			// 
			resources.ApplyResources(this.projectsToSubmitLabel, "projectsToSubmitLabel");
			this.projectsToSubmitLabel.MinimumSize = new System.Drawing.Size(150, 0);
			this.projectsToSubmitLabel.Name = "projectsToSubmitLabel";
			// 
			// submissionTargetsTree
			// 
			resources.ApplyResources(this.submissionTargetsTree, "submissionTargetsTree");
			this.submissionTargetsTree.HideSelection = false;
			this.submissionTargetsTree.Name = "submissionTargetsTree";
			this.submissionTargetsTree.BeforeExpand += new System.Windows.Forms.TreeViewCancelEventHandler(this.submissionTargetsTree_BeforeExpand);
			this.submissionTargetsTree.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.submissionTargetsTree_AfterSelect);
			// 
			// usernameField
			// 
			resources.ApplyResources(this.usernameField, "usernameField");
			this.usernameField.Name = "usernameField";
			this.usernameField.TextChanged += new System.EventHandler(this.usernameField_TextChanged);
			// 
			// passwordField
			// 
			resources.ApplyResources(this.passwordField, "passwordField");
			this.passwordField.Name = "passwordField";
			// 
			// cancelButton
			// 
			resources.ApplyResources(this.cancelButton, "cancelButton");
			this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.cancelButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.cancelButton.Name = "cancelButton";
			this.cancelButton.UseVisualStyleBackColor = true;
			// 
			// nextButton
			// 
			resources.ApplyResources(this.nextButton, "nextButton");
			this.nextButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.nextButton.Name = "nextButton";
			this.nextButton.UseVisualStyleBackColor = true;
			this.nextButton.Click += new System.EventHandler(this.nextButton_Click);
			// 
			// chooseProjectsToSubmit
			// 
			resources.ApplyResources(this.chooseProjectsToSubmit, "chooseProjectsToSubmit");
			this.chooseProjectsToSubmit.MinimumSize = new System.Drawing.Size(75, 0);
			this.chooseProjectsToSubmit.Name = "chooseProjectsToSubmit";
			this.chooseProjectsToSubmit.UseVisualStyleBackColor = true;
			this.chooseProjectsToSubmit.Click += new System.EventHandler(this.chooseProjectsToSubmit_Click);
			// 
			// projectsToSubmitField
			// 
			resources.ApplyResources(this.projectsToSubmitField, "projectsToSubmitField");
			this.projectsToSubmitField.Name = "projectsToSubmitField";
			this.projectsToSubmitField.ReadOnly = true;
			// 
			// passwordLabel
			// 
			resources.ApplyResources(this.passwordLabel, "passwordLabel");
			this.passwordLabel.MinimumSize = new System.Drawing.Size(150, 0);
			this.passwordLabel.Name = "passwordLabel";
			// 
			// usernameLabel
			// 
			resources.ApplyResources(this.usernameLabel, "usernameLabel");
			this.usernameLabel.MinimumSize = new System.Drawing.Size(150, 0);
			this.usernameLabel.Name = "usernameLabel";
			// 
			// assignmentsLabel
			// 
			resources.ApplyResources(this.assignmentsLabel, "assignmentsLabel");
			this.assignmentsLabel.MinimumSize = new System.Drawing.Size(150, 0);
			this.assignmentsLabel.Name = "assignmentsLabel";
			// 
			// sepStartPage2
			// 
			resources.ApplyResources(this.sepStartPage2, "sepStartPage2");
			this.sepStartPage2.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.sepStartPage2.Name = "sepStartPage2";
			// 
			// sepStartPage1
			// 
			resources.ApplyResources(this.sepStartPage1, "sepStartPage1");
			this.sepStartPage1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.sepStartPage1.Name = "sepStartPage1";
			// 
			// sepWizardBottom
			// 
			this.sepWizardBottom.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			resources.ApplyResources(this.sepWizardBottom, "sepWizardBottom");
			this.sepWizardBottom.Name = "sepWizardBottom";
			// 
			// wizardTitlePanel
			// 
			this.wizardTitlePanel.BackColor = System.Drawing.SystemColors.Window;
			this.wizardTitlePanel.Controls.Add(this.wizardDescriptionLabel);
			this.wizardTitlePanel.Controls.Add(this.wizardTitleLabel);
			resources.ApplyResources(this.wizardTitlePanel, "wizardTitlePanel");
			this.wizardTitlePanel.Name = "wizardTitlePanel";
			// 
			// wizardDescriptionLabel
			// 
			resources.ApplyResources(this.wizardDescriptionLabel, "wizardDescriptionLabel");
			this.wizardDescriptionLabel.Name = "wizardDescriptionLabel";
			// 
			// wizardTitleLabel
			// 
			resources.ApplyResources(this.wizardTitleLabel, "wizardTitleLabel");
			this.wizardTitleLabel.Name = "wizardTitleLabel";
			// 
			// sepWizardTop
			// 
			this.sepWizardTop.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			resources.ApplyResources(this.sepWizardTop, "sepWizardTop");
			this.sepWizardTop.Name = "sepWizardTop";
			// 
			// summaryPage
			// 
			this.summaryPage.Controls.Add(this.summaryDetailsLabel);
			this.summaryPage.Controls.Add(this.summaryDetailsField);
			this.summaryPage.Controls.Add(this.summaryLabel);
			this.summaryPage.Controls.Add(this.summaryImageLabel);
			resources.ApplyResources(this.summaryPage, "summaryPage");
			this.summaryPage.Name = "summaryPage";
			// 
			// summaryDetailsLabel
			// 
			resources.ApplyResources(this.summaryDetailsLabel, "summaryDetailsLabel");
			this.summaryDetailsLabel.Name = "summaryDetailsLabel";
			// 
			// summaryDetailsField
			// 
			this.summaryDetailsField.BackColor = System.Drawing.SystemColors.Window;
			resources.ApplyResources(this.summaryDetailsField, "summaryDetailsField");
			this.summaryDetailsField.Name = "summaryDetailsField";
			this.summaryDetailsField.ReadOnly = true;
			// 
			// summaryLabel
			// 
			resources.ApplyResources(this.summaryLabel, "summaryLabel");
			this.summaryLabel.Name = "summaryLabel";
			// 
			// summaryImageLabel
			// 
			resources.ApplyResources(this.summaryImageLabel, "summaryImageLabel");
			this.summaryImageLabel.Name = "summaryImageLabel";
			// 
			// startPage
			// 
			resources.ApplyResources(this.startPage, "startPage");
			this.startPage.Controls.Add(this.projectsToSubmitLabel, 0, 0);
			this.startPage.Controls.Add(this.submissionTargetsTree, 1, 2);
			this.startPage.Controls.Add(this.sepStartPage1, 1, 1);
			this.startPage.Controls.Add(this.assignmentsLabel, 0, 2);
			this.startPage.Controls.Add(this.passwordField, 1, 5);
			this.startPage.Controls.Add(this.usernameField, 1, 4);
			this.startPage.Controls.Add(this.passwordLabel, 0, 5);
			this.startPage.Controls.Add(this.sepStartPage2, 1, 3);
			this.startPage.Controls.Add(this.usernameLabel, 0, 4);
			this.startPage.Controls.Add(this.projectsToSubmitLayoutPanel, 1, 0);
			this.startPage.Name = "startPage";
			// 
			// projectsToSubmitLayoutPanel
			// 
			resources.ApplyResources(this.projectsToSubmitLayoutPanel, "projectsToSubmitLayoutPanel");
			this.projectsToSubmitLayoutPanel.Controls.Add(this.chooseProjectsToSubmit, 1, 0);
			this.projectsToSubmitLayoutPanel.Controls.Add(this.projectsToSubmitField, 0, 0);
			this.projectsToSubmitLayoutPanel.Name = "projectsToSubmitLayoutPanel";
			// 
			// wizardButtonsLayoutPanel
			// 
			resources.ApplyResources(this.wizardButtonsLayoutPanel, "wizardButtonsLayoutPanel");
			this.wizardButtonsLayoutPanel.Controls.Add(this.nextButton, 0, 0);
			this.wizardButtonsLayoutPanel.Controls.Add(this.cancelButton, 1, 0);
			this.wizardButtonsLayoutPanel.Name = "wizardButtonsLayoutPanel";
			// 
			// SubmitterWizard
			// 
			this.AcceptButton = this.nextButton;
			resources.ApplyResources(this, "$this");
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.CancelButton = this.cancelButton;
			this.Controls.Add(this.wizardButtonsLayoutPanel);
			this.Controls.Add(this.startPage);
			this.Controls.Add(this.summaryPage);
			this.Controls.Add(this.sepWizardTop);
			this.Controls.Add(this.wizardTitlePanel);
			this.Controls.Add(this.sepWizardBottom);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "SubmitterWizard";
			this.ShowInTaskbar = false;
			this.Load += new System.EventHandler(this.SubmitterWizard_Load);
			this.wizardTitlePanel.ResumeLayout(false);
			this.wizardTitlePanel.PerformLayout();
			this.summaryPage.ResumeLayout(false);
			this.summaryPage.PerformLayout();
			this.startPage.ResumeLayout(false);
			this.startPage.PerformLayout();
			this.projectsToSubmitLayoutPanel.ResumeLayout(false);
			this.projectsToSubmitLayoutPanel.PerformLayout();
			this.wizardButtonsLayoutPanel.ResumeLayout(false);
			this.wizardButtonsLayoutPanel.PerformLayout();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label projectsToSubmitLabel;
		private System.Windows.Forms.TreeView submissionTargetsTree;
		private System.Windows.Forms.TextBox usernameField;
		private System.Windows.Forms.TextBox passwordField;
		private System.Windows.Forms.Button cancelButton;
		private System.Windows.Forms.Button nextButton;
		private System.Windows.Forms.Label sepWizardBottom;
		private System.Windows.Forms.Label sepStartPage1;
		private System.Windows.Forms.Panel wizardTitlePanel;
		private System.Windows.Forms.Label wizardTitleLabel;
		private System.Windows.Forms.Label passwordLabel;
		private System.Windows.Forms.Label usernameLabel;
		private System.Windows.Forms.Label assignmentsLabel;
		private System.Windows.Forms.Label sepStartPage2;
		private System.Windows.Forms.Label wizardDescriptionLabel;
		private System.Windows.Forms.Label sepWizardTop;
		private System.Windows.Forms.Panel summaryPage;
		private System.Windows.Forms.Label summaryLabel;
		private System.Windows.Forms.Label summaryImageLabel;
		private System.Windows.Forms.TextBox summaryDetailsField;
		private System.Windows.Forms.Button chooseProjectsToSubmit;
		private System.Windows.Forms.TextBox projectsToSubmitField;
		private System.Windows.Forms.Label summaryDetailsLabel;
		private System.Windows.Forms.TableLayoutPanel startPage;
		private System.Windows.Forms.TableLayoutPanel projectsToSubmitLayoutPanel;
		private System.Windows.Forms.TableLayoutPanel wizardButtonsLayoutPanel;
	}
}