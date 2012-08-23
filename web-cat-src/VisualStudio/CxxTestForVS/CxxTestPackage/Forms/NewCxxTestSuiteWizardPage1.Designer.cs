/*==========================================================================*\
 |  $Id: NewCxxTestSuiteWizardPage1.Designer.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	partial class NewCxxTestSuiteWizardPage1
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
			this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
			this.label4 = new System.Windows.Forms.Label();
			this.label5 = new System.Windows.Forms.Label();
			this.nameField = new System.Windows.Forms.TextBox();
			this.label6 = new System.Windows.Forms.Label();
			this.superclassField = new System.Windows.Forms.TextBox();
			this.label7 = new System.Windows.Forms.Label();
			this.setUpButton = new System.Windows.Forms.CheckBox();
			this.tearDownButton = new System.Windows.Forms.CheckBox();
			this.label8 = new System.Windows.Forms.Label();
			this.label9 = new System.Windows.Forms.Label();
			this.headerUnderTestField = new System.Windows.Forms.TextBox();
			this.headerUnderTestBrowseButton = new System.Windows.Forms.Button();
			this.existingProjectsCombo = new System.Windows.Forms.ComboBox();
			this.label1 = new System.Windows.Forms.Label();
			this.tableLayoutPanel1.SuspendLayout();
			this.SuspendLayout();
			// 
			// tableLayoutPanel1
			// 
			this.tableLayoutPanel1.ColumnCount = 3;
			this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
			this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
			this.tableLayoutPanel1.Controls.Add(this.label4, 0, 1);
			this.tableLayoutPanel1.Controls.Add(this.label5, 0, 2);
			this.tableLayoutPanel1.Controls.Add(this.nameField, 1, 2);
			this.tableLayoutPanel1.Controls.Add(this.label6, 0, 3);
			this.tableLayoutPanel1.Controls.Add(this.superclassField, 1, 3);
			this.tableLayoutPanel1.Controls.Add(this.label7, 0, 4);
			this.tableLayoutPanel1.Controls.Add(this.setUpButton, 1, 5);
			this.tableLayoutPanel1.Controls.Add(this.tearDownButton, 1, 6);
			this.tableLayoutPanel1.Controls.Add(this.label8, 0, 7);
			this.tableLayoutPanel1.Controls.Add(this.label9, 0, 9);
			this.tableLayoutPanel1.Controls.Add(this.headerUnderTestField, 1, 9);
			this.tableLayoutPanel1.Controls.Add(this.headerUnderTestBrowseButton, 2, 9);
			this.tableLayoutPanel1.Controls.Add(this.existingProjectsCombo, 1, 0);
			this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
			this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
			this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
			this.tableLayoutPanel1.Name = "tableLayoutPanel1";
			this.tableLayoutPanel1.RowCount = 11;
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel1.Size = new System.Drawing.Size(490, 229);
			this.tableLayoutPanel1.TabIndex = 1;
			// 
			// label4
			// 
			this.label4.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.tableLayoutPanel1.SetColumnSpan(this.label4, 3);
			this.label4.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.label4.Location = new System.Drawing.Point(3, 30);
			this.label4.Margin = new System.Windows.Forms.Padding(3);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(484, 2);
			this.label4.TabIndex = 1;
			// 
			// label5
			// 
			this.label5.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.label5.AutoSize = true;
			this.label5.Location = new System.Drawing.Point(3, 42);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(38, 13);
			this.label5.TabIndex = 2;
			this.label5.Text = "Name:";
			// 
			// nameField
			// 
			this.nameField.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.nameField.Location = new System.Drawing.Point(108, 38);
			this.nameField.Name = "nameField";
			this.nameField.Size = new System.Drawing.Size(298, 21);
			this.nameField.TabIndex = 3;
			// 
			// label6
			// 
			this.label6.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.label6.AutoSize = true;
			this.label6.Location = new System.Drawing.Point(3, 69);
			this.label6.Name = "label6";
			this.label6.Size = new System.Drawing.Size(62, 13);
			this.label6.TabIndex = 4;
			this.label6.Text = "Superclass:";
			// 
			// superclassField
			// 
			this.superclassField.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.superclassField.Location = new System.Drawing.Point(108, 65);
			this.superclassField.Name = "superclassField";
			this.superclassField.Size = new System.Drawing.Size(298, 21);
			this.superclassField.TabIndex = 5;
			// 
			// label7
			// 
			this.label7.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.label7.AutoSize = true;
			this.tableLayoutPanel1.SetColumnSpan(this.label7, 3);
			this.label7.Location = new System.Drawing.Point(3, 95);
			this.label7.Margin = new System.Windows.Forms.Padding(3, 6, 3, 3);
			this.label7.Name = "label7";
			this.label7.Size = new System.Drawing.Size(226, 13);
			this.label7.TabIndex = 6;
			this.label7.Text = "Which method stubs would you like to create?";
			// 
			// setUpButton
			// 
			this.setUpButton.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.setUpButton.AutoSize = true;
			this.setUpButton.Location = new System.Drawing.Point(108, 114);
			this.setUpButton.Name = "setUpButton";
			this.setUpButton.Size = new System.Drawing.Size(62, 17);
			this.setUpButton.TabIndex = 7;
			this.setUpButton.Text = "setUp()";
			this.setUpButton.UseVisualStyleBackColor = true;
			// 
			// tearDownButton
			// 
			this.tearDownButton.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.tearDownButton.AutoSize = true;
			this.tearDownButton.Location = new System.Drawing.Point(108, 137);
			this.tearDownButton.Name = "tearDownButton";
			this.tearDownButton.Size = new System.Drawing.Size(81, 17);
			this.tearDownButton.TabIndex = 8;
			this.tearDownButton.Text = "tearDown()";
			this.tearDownButton.UseVisualStyleBackColor = true;
			// 
			// label8
			// 
			this.label8.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.tableLayoutPanel1.SetColumnSpan(this.label8, 3);
			this.label8.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.label8.Location = new System.Drawing.Point(3, 160);
			this.label8.Margin = new System.Windows.Forms.Padding(3);
			this.label8.Name = "label8";
			this.label8.Size = new System.Drawing.Size(484, 2);
			this.label8.TabIndex = 9;
			// 
			// label9
			// 
			this.label9.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.label9.AutoSize = true;
			this.label9.Location = new System.Drawing.Point(3, 173);
			this.label9.Name = "label9";
			this.label9.Size = new System.Drawing.Size(99, 13);
			this.label9.TabIndex = 10;
			this.label9.Text = "Header under test:";
			// 
			// headerUnderTestField
			// 
			this.headerUnderTestField.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.headerUnderTestField.Location = new System.Drawing.Point(108, 169);
			this.headerUnderTestField.Name = "headerUnderTestField";
			this.headerUnderTestField.Size = new System.Drawing.Size(298, 21);
			this.headerUnderTestField.TabIndex = 11;
			// 
			// headerUnderTestBrowseButton
			// 
			this.headerUnderTestBrowseButton.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.headerUnderTestBrowseButton.Location = new System.Drawing.Point(412, 168);
			this.headerUnderTestBrowseButton.Name = "headerUnderTestBrowseButton";
			this.headerUnderTestBrowseButton.Size = new System.Drawing.Size(75, 23);
			this.headerUnderTestBrowseButton.TabIndex = 12;
			this.headerUnderTestBrowseButton.Text = "Browse...";
			this.headerUnderTestBrowseButton.UseVisualStyleBackColor = true;
			this.headerUnderTestBrowseButton.Click += new System.EventHandler(this.headerUnderTestBrowseButton_Click);
			// 
			// existingProjectsCombo
			// 
			this.existingProjectsCombo.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.existingProjectsCombo.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.existingProjectsCombo.FormattingEnabled = true;
			this.existingProjectsCombo.Location = new System.Drawing.Point(108, 3);
			this.existingProjectsCombo.Name = "existingProjectsCombo";
			this.existingProjectsCombo.Size = new System.Drawing.Size(298, 21);
			this.existingProjectsCombo.TabIndex = 13;
			// 
			// label1
			// 
			this.label1.Anchor = System.Windows.Forms.AnchorStyles.Left;
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(3, 7);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(80, 13);
			this.label1.TabIndex = 14;
			this.label1.Text = "Add to Project:";
			// 
			// NewCxxTestSuiteWizardPage1
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.tableLayoutPanel1);
			this.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.Name = "NewCxxTestSuiteWizardPage1";
			this.Size = new System.Drawing.Size(490, 229);
			this.tableLayoutPanel1.ResumeLayout(false);
			this.tableLayoutPanel1.PerformLayout();
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.TextBox nameField;
		private System.Windows.Forms.Label label6;
		private System.Windows.Forms.TextBox superclassField;
		private System.Windows.Forms.Label label7;
		private System.Windows.Forms.CheckBox setUpButton;
		private System.Windows.Forms.CheckBox tearDownButton;
		private System.Windows.Forms.Label label8;
		private System.Windows.Forms.Label label9;
		private System.Windows.Forms.TextBox headerUnderTestField;
		private System.Windows.Forms.Button headerUnderTestBrowseButton;
		private System.Windows.Forms.ComboBox existingProjectsCombo;
		private System.Windows.Forms.Label label1;
	}
}
