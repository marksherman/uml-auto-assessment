/*==========================================================================*\
 |  $Id: SubmitterOptionsPanel.designer.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
	partial class SubmitterOptionsPanel
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

		#region Component Designer generated code

		/// <summary> 
		/// Required method for Designer support - do not modify 
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SubmitterOptionsPanel));
			this.submissionDefsUrlField = new System.Windows.Forms.TextBox();
			this.mailServerField = new System.Windows.Forms.TextBox();
			this.mailServerLabel = new System.Windows.Forms.Label();
			this.defaultUsernameField = new System.Windows.Forms.TextBox();
			this.defaultUsernameLabel = new System.Windows.Forms.Label();
			this.submissionDefsUrlLabel = new System.Windows.Forms.Label();
			this.separator1 = new System.Windows.Forms.Label();
			this.emailAddressField = new System.Windows.Forms.TextBox();
			this.emailAddressLabel = new System.Windows.Forms.Label();
			this.emailDescriptionLabel = new System.Windows.Forms.Label();
			this.tableLayoutPanel = new System.Windows.Forms.TableLayoutPanel();
			this.tableLayoutPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// submissionDefsUrlField
			// 
			resources.ApplyResources(this.submissionDefsUrlField, "submissionDefsUrlField");
			this.submissionDefsUrlField.Name = "submissionDefsUrlField";
			this.submissionDefsUrlField.TextChanged += new System.EventHandler(this.submissionDefsUrlField_TextChanged);
			// 
			// mailServerField
			// 
			resources.ApplyResources(this.mailServerField, "mailServerField");
			this.mailServerField.Name = "mailServerField";
			this.mailServerField.TextChanged += new System.EventHandler(this.mailServerField_TextChanged);
			// 
			// mailServerLabel
			// 
			resources.ApplyResources(this.mailServerLabel, "mailServerLabel");
			this.mailServerLabel.Name = "mailServerLabel";
			// 
			// defaultUsernameField
			// 
			resources.ApplyResources(this.defaultUsernameField, "defaultUsernameField");
			this.defaultUsernameField.Name = "defaultUsernameField";
			this.defaultUsernameField.TextChanged += new System.EventHandler(this.defaultUsernameField_TextChanged);
			// 
			// defaultUsernameLabel
			// 
			resources.ApplyResources(this.defaultUsernameLabel, "defaultUsernameLabel");
			this.defaultUsernameLabel.Name = "defaultUsernameLabel";
			// 
			// submissionDefsUrlLabel
			// 
			resources.ApplyResources(this.submissionDefsUrlLabel, "submissionDefsUrlLabel");
			this.submissionDefsUrlLabel.Name = "submissionDefsUrlLabel";
			// 
			// separator1
			// 
			this.separator1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.tableLayoutPanel.SetColumnSpan(this.separator1, 2);
			resources.ApplyResources(this.separator1, "separator1");
			this.separator1.Name = "separator1";
			// 
			// emailAddressField
			// 
			resources.ApplyResources(this.emailAddressField, "emailAddressField");
			this.emailAddressField.Name = "emailAddressField";
			this.emailAddressField.TextChanged += new System.EventHandler(this.emailAddressField_TextChanged);
			// 
			// emailAddressLabel
			// 
			resources.ApplyResources(this.emailAddressLabel, "emailAddressLabel");
			this.emailAddressLabel.Name = "emailAddressLabel";
			// 
			// emailDescriptionLabel
			// 
			resources.ApplyResources(this.emailDescriptionLabel, "emailDescriptionLabel");
			this.tableLayoutPanel.SetColumnSpan(this.emailDescriptionLabel, 2);
			this.emailDescriptionLabel.Name = "emailDescriptionLabel";
			// 
			// tableLayoutPanel
			// 
			resources.ApplyResources(this.tableLayoutPanel, "tableLayoutPanel");
			this.tableLayoutPanel.Controls.Add(this.submissionDefsUrlLabel, 0, 0);
			this.tableLayoutPanel.Controls.Add(this.emailAddressField, 1, 5);
			this.tableLayoutPanel.Controls.Add(this.emailDescriptionLabel, 0, 3);
			this.tableLayoutPanel.Controls.Add(this.mailServerField, 1, 4);
			this.tableLayoutPanel.Controls.Add(this.emailAddressLabel, 0, 5);
			this.tableLayoutPanel.Controls.Add(this.submissionDefsUrlField, 1, 0);
			this.tableLayoutPanel.Controls.Add(this.defaultUsernameLabel, 0, 1);
			this.tableLayoutPanel.Controls.Add(this.mailServerLabel, 0, 4);
			this.tableLayoutPanel.Controls.Add(this.defaultUsernameField, 1, 1);
			this.tableLayoutPanel.Controls.Add(this.separator1, 0, 2);
			this.tableLayoutPanel.Name = "tableLayoutPanel";
			// 
			// SubmitterOptionsPanel
			// 
			resources.ApplyResources(this, "$this");
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.tableLayoutPanel);
			this.Name = "SubmitterOptionsPanel";
			this.tableLayoutPanel.ResumeLayout(false);
			this.tableLayoutPanel.PerformLayout();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.TextBox submissionDefsUrlField;
		private System.Windows.Forms.TextBox mailServerField;
		private System.Windows.Forms.Label mailServerLabel;
		private System.Windows.Forms.TextBox defaultUsernameField;
		private System.Windows.Forms.Label defaultUsernameLabel;
		private System.Windows.Forms.Label submissionDefsUrlLabel;
		private System.Windows.Forms.Label separator1;
		private System.Windows.Forms.TextBox emailAddressField;
		private System.Windows.Forms.Label emailAddressLabel;
		private System.Windows.Forms.Label emailDescriptionLabel;
		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel;
	}
}
