/*==========================================================================*\
 |  $Id: WizardForm.Designer.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
	partial class WizardForm
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
			this.bannerPanel = new System.Windows.Forms.Panel();
			this.messageLabel = new System.Windows.Forms.Label();
			this.bannerSeparator = new System.Windows.Forms.Label();
			this.titleLabel = new System.Windows.Forms.Label();
			this.wizardButtonsLayoutPanel = new System.Windows.Forms.TableLayoutPanel();
			this.nextButton = new System.Windows.Forms.Button();
			this.backButton = new System.Windows.Forms.Button();
			this.cancelButton = new System.Windows.Forms.Button();
			this.finishButton = new System.Windows.Forms.Button();
			this.bottomSeparator = new System.Windows.Forms.Label();
			this.pageContainer = new System.Windows.Forms.Panel();
			this.bannerPanel.SuspendLayout();
			this.wizardButtonsLayoutPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// bannerPanel
			// 
			this.bannerPanel.BackColor = System.Drawing.SystemColors.Window;
			this.bannerPanel.Controls.Add(this.messageLabel);
			this.bannerPanel.Controls.Add(this.bannerSeparator);
			this.bannerPanel.Controls.Add(this.titleLabel);
			this.bannerPanel.Dock = System.Windows.Forms.DockStyle.Top;
			this.bannerPanel.Location = new System.Drawing.Point(0, 0);
			this.bannerPanel.Name = "bannerPanel";
			this.bannerPanel.Size = new System.Drawing.Size(400, 72);
			this.bannerPanel.TabIndex = 1;
			// 
			// messageLabel
			// 
			this.messageLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.messageLabel.Location = new System.Drawing.Point(16, 32);
			this.messageLabel.Name = "messageLabel";
			this.messageLabel.Size = new System.Drawing.Size(367, 31);
			this.messageLabel.TabIndex = 2;
			// 
			// bannerSeparator
			// 
			this.bannerSeparator.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.bannerSeparator.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.bannerSeparator.Location = new System.Drawing.Point(0, 70);
			this.bannerSeparator.Name = "bannerSeparator";
			this.bannerSeparator.Size = new System.Drawing.Size(400, 2);
			this.bannerSeparator.TabIndex = 1;
			// 
			// titleLabel
			// 
			this.titleLabel.AutoSize = true;
			this.titleLabel.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.titleLabel.Location = new System.Drawing.Point(8, 8);
			this.titleLabel.Name = "titleLabel";
			this.titleLabel.Size = new System.Drawing.Size(0, 13);
			this.titleLabel.TabIndex = 0;
			// 
			// wizardButtonsLayoutPanel
			// 
			this.wizardButtonsLayoutPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.wizardButtonsLayoutPanel.AutoSize = true;
			this.wizardButtonsLayoutPanel.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
			this.wizardButtonsLayoutPanel.ColumnCount = 4;
			this.wizardButtonsLayoutPanel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
			this.wizardButtonsLayoutPanel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
			this.wizardButtonsLayoutPanel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
			this.wizardButtonsLayoutPanel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
			this.wizardButtonsLayoutPanel.Controls.Add(this.nextButton, 0, 0);
			this.wizardButtonsLayoutPanel.Controls.Add(this.backButton, 0, 0);
			this.wizardButtonsLayoutPanel.Controls.Add(this.cancelButton, 3, 0);
			this.wizardButtonsLayoutPanel.Controls.Add(this.finishButton, 1, 0);
			this.wizardButtonsLayoutPanel.Location = new System.Drawing.Point(67, 264);
			this.wizardButtonsLayoutPanel.Name = "wizardButtonsLayoutPanel";
			this.wizardButtonsLayoutPanel.RowCount = 1;
			this.wizardButtonsLayoutPanel.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.wizardButtonsLayoutPanel.Size = new System.Drawing.Size(324, 29);
			this.wizardButtonsLayoutPanel.TabIndex = 11;
			// 
			// nextButton
			// 
			this.nextButton.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.nextButton.AutoSize = true;
			this.nextButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
			this.nextButton.ImeMode = System.Windows.Forms.ImeMode.NoControl;
			this.nextButton.Location = new System.Drawing.Point(84, 3);
			this.nextButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.nextButton.Name = "nextButton";
			this.nextButton.Size = new System.Drawing.Size(75, 23);
			this.nextButton.TabIndex = 6;
			this.nextButton.Text = "Next >";
			this.nextButton.UseVisualStyleBackColor = true;
			this.nextButton.Click += new System.EventHandler(this.nextButton_Click);
			// 
			// backButton
			// 
			this.backButton.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.backButton.AutoSize = true;
			this.backButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
			this.backButton.ImeMode = System.Windows.Forms.ImeMode.NoControl;
			this.backButton.Location = new System.Drawing.Point(3, 3);
			this.backButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.backButton.Name = "backButton";
			this.backButton.Size = new System.Drawing.Size(75, 23);
			this.backButton.TabIndex = 5;
			this.backButton.Text = "< Back";
			this.backButton.UseVisualStyleBackColor = true;
			this.backButton.Click += new System.EventHandler(this.backButton_Click);
			// 
			// cancelButton
			// 
			this.cancelButton.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.cancelButton.AutoSize = true;
			this.cancelButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
			this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.cancelButton.ImeMode = System.Windows.Forms.ImeMode.NoControl;
			this.cancelButton.Location = new System.Drawing.Point(246, 3);
			this.cancelButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.cancelButton.Name = "cancelButton";
			this.cancelButton.Size = new System.Drawing.Size(75, 23);
			this.cancelButton.TabIndex = 4;
			this.cancelButton.Text = "Cancel";
			this.cancelButton.UseVisualStyleBackColor = true;
			// 
			// finishButton
			// 
			this.finishButton.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.finishButton.AutoSize = true;
			this.finishButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
			this.finishButton.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.finishButton.ImeMode = System.Windows.Forms.ImeMode.NoControl;
			this.finishButton.Location = new System.Drawing.Point(165, 3);
			this.finishButton.MinimumSize = new System.Drawing.Size(75, 0);
			this.finishButton.Name = "finishButton";
			this.finishButton.Size = new System.Drawing.Size(75, 23);
			this.finishButton.TabIndex = 3;
			this.finishButton.Text = "Finish";
			this.finishButton.UseVisualStyleBackColor = true;
			this.finishButton.Click += new System.EventHandler(this.finishButton_Click);
			// 
			// bottomSeparator
			// 
			this.bottomSeparator.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.bottomSeparator.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.bottomSeparator.ImeMode = System.Windows.Forms.ImeMode.NoControl;
			this.bottomSeparator.Location = new System.Drawing.Point(0, 255);
			this.bottomSeparator.Name = "bottomSeparator";
			this.bottomSeparator.Size = new System.Drawing.Size(401, 2);
			this.bottomSeparator.TabIndex = 10;
			// 
			// pageContainer
			// 
			this.pageContainer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
						| System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.pageContainer.Location = new System.Drawing.Point(8, 80);
			this.pageContainer.Name = "pageContainer";
			this.pageContainer.Size = new System.Drawing.Size(384, 168);
			this.pageContainer.TabIndex = 12;
			// 
			// WizardForm
			// 
			this.AcceptButton = this.finishButton;
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.CancelButton = this.cancelButton;
			this.ClientSize = new System.Drawing.Size(400, 302);
			this.Controls.Add(this.pageContainer);
			this.Controls.Add(this.wizardButtonsLayoutPanel);
			this.Controls.Add(this.bottomSeparator);
			this.Controls.Add(this.bannerPanel);
			this.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "WizardForm";
			this.ShowIcon = false;
			this.ShowInTaskbar = false;
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
			this.Text = " ";
			this.bannerPanel.ResumeLayout(false);
			this.bannerPanel.PerformLayout();
			this.wizardButtonsLayoutPanel.ResumeLayout(false);
			this.wizardButtonsLayoutPanel.PerformLayout();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Panel bannerPanel;
		private System.Windows.Forms.Label bannerSeparator;
		private System.Windows.Forms.Label messageLabel;
		private System.Windows.Forms.Label titleLabel;
		private System.Windows.Forms.TableLayoutPanel wizardButtonsLayoutPanel;
		private System.Windows.Forms.Button nextButton;
		private System.Windows.Forms.Button backButton;
		private System.Windows.Forms.Button cancelButton;
		private System.Windows.Forms.Button finishButton;
		private System.Windows.Forms.Label bottomSeparator;
		private System.Windows.Forms.Panel pageContainer;
	}
}