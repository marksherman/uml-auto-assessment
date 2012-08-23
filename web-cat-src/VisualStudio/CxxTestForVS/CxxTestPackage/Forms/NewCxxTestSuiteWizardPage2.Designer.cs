/*==========================================================================*\
 |  $Id: NewCxxTestSuiteWizardPage2.Designer.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
	partial class NewCxxTestSuiteWizardPage2
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
			this.components = new System.ComponentModel.Container();
			this.tableLayoutPanel3 = new System.Windows.Forms.TableLayoutPanel();
			this.deselectAllFunctionsButton = new System.Windows.Forms.Button();
			this.label10 = new System.Windows.Forms.Label();
			this.functionsTree = new System.Windows.Forms.TreeView();
			this.functionsSelectedLabel = new System.Windows.Forms.Label();
			this.selectAllFunctionsButton = new System.Windows.Forms.Button();
			this.imageList = new System.Windows.Forms.ImageList(this.components);
			this.tableLayoutPanel3.SuspendLayout();
			this.SuspendLayout();
			// 
			// tableLayoutPanel3
			// 
			this.tableLayoutPanel3.ColumnCount = 2;
			this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
			this.tableLayoutPanel3.Controls.Add(this.deselectAllFunctionsButton, 1, 2);
			this.tableLayoutPanel3.Controls.Add(this.label10, 0, 0);
			this.tableLayoutPanel3.Controls.Add(this.functionsTree, 0, 1);
			this.tableLayoutPanel3.Controls.Add(this.functionsSelectedLabel, 0, 3);
			this.tableLayoutPanel3.Controls.Add(this.selectAllFunctionsButton, 1, 1);
			this.tableLayoutPanel3.Dock = System.Windows.Forms.DockStyle.Fill;
			this.tableLayoutPanel3.Location = new System.Drawing.Point(0, 0);
			this.tableLayoutPanel3.Name = "tableLayoutPanel3";
			this.tableLayoutPanel3.RowCount = 4;
			this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle());
			this.tableLayoutPanel3.Size = new System.Drawing.Size(490, 275);
			this.tableLayoutPanel3.TabIndex = 1;
			// 
			// deselectAllFunctionsButton
			// 
			this.deselectAllFunctionsButton.Location = new System.Drawing.Point(412, 48);
			this.deselectAllFunctionsButton.Name = "deselectAllFunctionsButton";
			this.deselectAllFunctionsButton.Size = new System.Drawing.Size(75, 23);
			this.deselectAllFunctionsButton.TabIndex = 4;
			this.deselectAllFunctionsButton.Text = "Deselect All";
			this.deselectAllFunctionsButton.UseVisualStyleBackColor = true;
			// 
			// label10
			// 
			this.label10.AutoSize = true;
			this.tableLayoutPanel3.SetColumnSpan(this.label10, 2);
			this.label10.Location = new System.Drawing.Point(3, 0);
			this.label10.Margin = new System.Windows.Forms.Padding(3, 0, 3, 3);
			this.label10.Name = "label10";
			this.label10.Size = new System.Drawing.Size(166, 13);
			this.label10.TabIndex = 0;
			this.label10.Text = "Available functions and methods:";
			// 
			// functionsTree
			// 
			this.functionsTree.CheckBoxes = true;
			this.functionsTree.Dock = System.Windows.Forms.DockStyle.Fill;
			this.functionsTree.ImageIndex = 0;
			this.functionsTree.ImageList = this.imageList;
			this.functionsTree.Location = new System.Drawing.Point(3, 19);
			this.functionsTree.Name = "functionsTree";
			this.tableLayoutPanel3.SetRowSpan(this.functionsTree, 2);
			this.functionsTree.SelectedImageIndex = 0;
			this.functionsTree.Size = new System.Drawing.Size(403, 237);
			this.functionsTree.TabIndex = 1;
			this.functionsTree.AfterCheck += new System.Windows.Forms.TreeViewEventHandler(this.functionsTree_AfterCheck);
			// 
			// functionsSelectedLabel
			// 
			this.functionsSelectedLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.functionsSelectedLabel.AutoSize = true;
			this.tableLayoutPanel3.SetColumnSpan(this.functionsSelectedLabel, 2);
			this.functionsSelectedLabel.Location = new System.Drawing.Point(3, 259);
			this.functionsSelectedLabel.Name = "functionsSelectedLabel";
			this.functionsSelectedLabel.Padding = new System.Windows.Forms.Padding(0, 3, 0, 0);
			this.functionsSelectedLabel.Size = new System.Drawing.Size(484, 16);
			this.functionsSelectedLabel.TabIndex = 2;
			this.functionsSelectedLabel.Text = "0 functions/methods selected.";
			// 
			// selectAllFunctionsButton
			// 
			this.selectAllFunctionsButton.Location = new System.Drawing.Point(412, 19);
			this.selectAllFunctionsButton.Name = "selectAllFunctionsButton";
			this.selectAllFunctionsButton.Size = new System.Drawing.Size(75, 23);
			this.selectAllFunctionsButton.TabIndex = 3;
			this.selectAllFunctionsButton.Text = "Select All";
			this.selectAllFunctionsButton.UseVisualStyleBackColor = true;
			// 
			// imageList
			// 
			this.imageList.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
			this.imageList.ImageSize = new System.Drawing.Size(16, 16);
			this.imageList.TransparentColor = System.Drawing.Color.Transparent;
			// 
			// NewCxxTestSuiteWizardPage2
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.tableLayoutPanel3);
			this.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.Name = "NewCxxTestSuiteWizardPage2";
			this.Size = new System.Drawing.Size(490, 275);
			this.tableLayoutPanel3.ResumeLayout(false);
			this.tableLayoutPanel3.PerformLayout();
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel3;
		private System.Windows.Forms.Button deselectAllFunctionsButton;
		private System.Windows.Forms.Label label10;
		private System.Windows.Forms.TreeView functionsTree;
		private System.Windows.Forms.Label functionsSelectedLabel;
		private System.Windows.Forms.Button selectAllFunctionsButton;
		private System.Windows.Forms.ImageList imageList;
	}
}
