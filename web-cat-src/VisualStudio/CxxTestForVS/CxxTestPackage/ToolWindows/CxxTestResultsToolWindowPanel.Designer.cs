/*==========================================================================*\
 |  $Id: CxxTestResultsToolWindowPanel.Designer.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.ToolWindows
{
    partial class CxxTestResultsToolWindowPanel
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
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CxxTestResultsToolWindowPanel));
			this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
			this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
			this.columnHeader3 = new System.Windows.Forms.ColumnHeader();
			this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
			this.splitContainer2 = new System.Windows.Forms.SplitContainer();
			this.masterView = new System.Windows.Forms.TreeView();
			this.masterDetailImages = new System.Windows.Forms.ImageList(this.components);
			this.masterToolStrip = new System.Windows.Forms.ToolStrip();
			this.hierarchyButton = new System.Windows.Forms.ToolStripButton();
			this.memoryButton = new System.Windows.Forms.ToolStripButton();
			this.detailsView = new System.Windows.Forms.TreeView();
			this.detailsToolStrip = new System.Windows.Forms.ToolStrip();
			this.toolStripLabel1 = new System.Windows.Forms.ToolStripLabel();
			this.tableLayoutPanel3 = new System.Windows.Forms.TableLayoutPanel();
			this.tableLayoutPanel4 = new System.Windows.Forms.TableLayoutPanel();
			this.runsLabel = new System.Windows.Forms.Label();
			this.errorsLabel = new System.Windows.Forms.Label();
			this.failuresLabel = new System.Windows.Forms.Label();
			this.toolStripImages = new System.Windows.Forms.ImageList(this.components);
			this.testResultsProgress = new WebCAT.CxxTest.VisualStudio.Controls.PassFailBar();
			this.tableLayoutPanel2.SuspendLayout();
			this.splitContainer2.Panel1.SuspendLayout();
			this.splitContainer2.Panel2.SuspendLayout();
			this.splitContainer2.SuspendLayout();
			this.masterToolStrip.SuspendLayout();
			this.detailsToolStrip.SuspendLayout();
			this.tableLayoutPanel3.SuspendLayout();
			this.tableLayoutPanel4.SuspendLayout();
			this.SuspendLayout();
			// 
			// columnHeader1
			// 
			this.columnHeader1.Text = "";
			this.columnHeader1.Width = 25;
			// 
			// columnHeader2
			// 
			this.columnHeader2.Text = "Test Name";
			this.columnHeader2.Width = 120;
			// 
			// columnHeader3
			// 
			this.columnHeader3.Text = "Result Description";
			this.columnHeader3.Width = 257;
			// 
			// tableLayoutPanel2
			// 
			this.tableLayoutPanel2.ColumnCount = 1;
			this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel2.Controls.Add(this.splitContainer2, 0, 1);
			this.tableLayoutPanel2.Controls.Add(this.tableLayoutPanel3, 0, 0);
			this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
			this.tableLayoutPanel2.Location = new System.Drawing.Point(0, 0);
			this.tableLayoutPanel2.Margin = new System.Windows.Forms.Padding(0);
			this.tableLayoutPanel2.Name = "tableLayoutPanel2";
			this.tableLayoutPanel2.RowCount = 2;
			this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 30F));
			this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel2.Size = new System.Drawing.Size(667, 195);
			this.tableLayoutPanel2.TabIndex = 2;
			// 
			// splitContainer2
			// 
			this.splitContainer2.Dock = System.Windows.Forms.DockStyle.Fill;
			this.splitContainer2.Location = new System.Drawing.Point(0, 30);
			this.splitContainer2.Margin = new System.Windows.Forms.Padding(0);
			this.splitContainer2.Name = "splitContainer2";
			// 
			// splitContainer2.Panel1
			// 
			this.splitContainer2.Panel1.Controls.Add(this.masterView);
			this.splitContainer2.Panel1.Controls.Add(this.masterToolStrip);
			// 
			// splitContainer2.Panel2
			// 
			this.splitContainer2.Panel2.Controls.Add(this.detailsView);
			this.splitContainer2.Panel2.Controls.Add(this.detailsToolStrip);
			this.splitContainer2.Size = new System.Drawing.Size(667, 165);
			this.splitContainer2.SplitterDistance = 330;
			this.splitContainer2.TabIndex = 0;
			// 
			// masterView
			// 
			this.masterView.Dock = System.Windows.Forms.DockStyle.Fill;
			this.masterView.ImageIndex = 0;
			this.masterView.ImageList = this.masterDetailImages;
			this.masterView.Location = new System.Drawing.Point(0, 25);
			this.masterView.Name = "masterView";
			this.masterView.SelectedImageIndex = 0;
			this.masterView.Size = new System.Drawing.Size(330, 140);
			this.masterView.TabIndex = 1;
			this.masterView.NodeMouseDoubleClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.masterView_NodeMouseDoubleClick);
			this.masterView.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.masterView_AfterSelect);
			// 
			// masterDetailImages
			// 
			this.masterDetailImages.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("masterDetailImages.ImageStream")));
			this.masterDetailImages.TransparentColor = System.Drawing.Color.Transparent;
			this.masterDetailImages.Images.SetKeyName(0, "Suite");
			this.masterDetailImages.Images.SetKeyName(1, "SuiteOK");
			this.masterDetailImages.Images.SetKeyName(2, "SuiteWarning");
			this.masterDetailImages.Images.SetKeyName(3, "SuiteError");
			this.masterDetailImages.Images.SetKeyName(4, "SuiteFailure");
			this.masterDetailImages.Images.SetKeyName(5, "Test");
			this.masterDetailImages.Images.SetKeyName(6, "TestOK");
			this.masterDetailImages.Images.SetKeyName(7, "TestWarning");
			this.masterDetailImages.Images.SetKeyName(8, "TestError");
			this.masterDetailImages.Images.SetKeyName(9, "TestFailure");
			this.masterDetailImages.Images.SetKeyName(10, "AssertTrace");
			this.masterDetailImages.Images.SetKeyName(11, "AssertWarning");
			this.masterDetailImages.Images.SetKeyName(12, "AssertError");
			this.masterDetailImages.Images.SetKeyName(13, "AssertFailure");
			this.masterDetailImages.Images.SetKeyName(14, "BacktraceFrame");
			this.masterDetailImages.Images.SetKeyName(15, "Leaks");
			this.masterDetailImages.Images.SetKeyName(16, "LeakNonArray");
			this.masterDetailImages.Images.SetKeyName(17, "LeakArray");
			// 
			// masterToolStrip
			// 
			this.masterToolStrip.GripStyle = System.Windows.Forms.ToolStripGripStyle.Hidden;
			this.masterToolStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.hierarchyButton,
            this.memoryButton});
			this.masterToolStrip.Location = new System.Drawing.Point(0, 0);
			this.masterToolStrip.Name = "masterToolStrip";
			this.masterToolStrip.RenderMode = System.Windows.Forms.ToolStripRenderMode.System;
			this.masterToolStrip.Size = new System.Drawing.Size(330, 25);
			this.masterToolStrip.TabIndex = 0;
			this.masterToolStrip.Text = "toolStrip1";
			// 
			// hierarchyButton
			// 
			this.hierarchyButton.CheckOnClick = true;
			this.hierarchyButton.Image = ((System.Drawing.Image)(resources.GetObject("hierarchyButton.Image")));
			this.hierarchyButton.ImageTransparentColor = System.Drawing.Color.Magenta;
			this.hierarchyButton.Name = "hierarchyButton";
			this.hierarchyButton.Size = new System.Drawing.Size(73, 22);
			this.hierarchyButton.Text = "Hierarchy";
			this.hierarchyButton.CheckedChanged += new System.EventHandler(this.hierarchyButton_CheckedChanged);
			// 
			// memoryButton
			// 
			this.memoryButton.CheckOnClick = true;
			this.memoryButton.Image = ((System.Drawing.Image)(resources.GetObject("memoryButton.Image")));
			this.memoryButton.ImageTransparentColor = System.Drawing.Color.Magenta;
			this.memoryButton.Name = "memoryButton";
			this.memoryButton.Size = new System.Drawing.Size(65, 22);
			this.memoryButton.Text = "Memory";
			this.memoryButton.CheckedChanged += new System.EventHandler(this.memoryButton_CheckedChanged);
			// 
			// detailsView
			// 
			this.detailsView.Dock = System.Windows.Forms.DockStyle.Fill;
			this.detailsView.ImageIndex = 0;
			this.detailsView.ImageList = this.masterDetailImages;
			this.detailsView.Location = new System.Drawing.Point(0, 25);
			this.detailsView.Name = "detailsView";
			this.detailsView.SelectedImageIndex = 0;
			this.detailsView.Size = new System.Drawing.Size(333, 140);
			this.detailsView.TabIndex = 1;
			this.detailsView.NodeMouseDoubleClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.detailsView_NodeMouseDoubleClick);
			// 
			// detailsToolStrip
			// 
			this.detailsToolStrip.GripStyle = System.Windows.Forms.ToolStripGripStyle.Hidden;
			this.detailsToolStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripLabel1});
			this.detailsToolStrip.Location = new System.Drawing.Point(0, 0);
			this.detailsToolStrip.Name = "detailsToolStrip";
			this.detailsToolStrip.RenderMode = System.Windows.Forms.ToolStripRenderMode.System;
			this.detailsToolStrip.Size = new System.Drawing.Size(333, 25);
			this.detailsToolStrip.TabIndex = 0;
			this.detailsToolStrip.Text = "toolStrip2";
			// 
			// toolStripLabel1
			// 
			this.toolStripLabel1.Name = "toolStripLabel1";
			this.toolStripLabel1.Size = new System.Drawing.Size(43, 22);
			this.toolStripLabel1.Text = "Details:";
			// 
			// tableLayoutPanel3
			// 
			this.tableLayoutPanel3.ColumnCount = 2;
			this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
			this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
			this.tableLayoutPanel3.Controls.Add(this.testResultsProgress, 1, 0);
			this.tableLayoutPanel3.Controls.Add(this.tableLayoutPanel4, 0, 0);
			this.tableLayoutPanel3.Dock = System.Windows.Forms.DockStyle.Fill;
			this.tableLayoutPanel3.Location = new System.Drawing.Point(0, 0);
			this.tableLayoutPanel3.Margin = new System.Windows.Forms.Padding(0);
			this.tableLayoutPanel3.Name = "tableLayoutPanel3";
			this.tableLayoutPanel3.RowCount = 1;
			this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
			this.tableLayoutPanel3.Size = new System.Drawing.Size(667, 30);
			this.tableLayoutPanel3.TabIndex = 1;
			// 
			// tableLayoutPanel4
			// 
			this.tableLayoutPanel4.ColumnCount = 3;
			this.tableLayoutPanel4.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
			this.tableLayoutPanel4.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
			this.tableLayoutPanel4.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
			this.tableLayoutPanel4.Controls.Add(this.runsLabel, 0, 0);
			this.tableLayoutPanel4.Controls.Add(this.errorsLabel, 1, 0);
			this.tableLayoutPanel4.Controls.Add(this.failuresLabel, 2, 0);
			this.tableLayoutPanel4.Dock = System.Windows.Forms.DockStyle.Fill;
			this.tableLayoutPanel4.Location = new System.Drawing.Point(0, 0);
			this.tableLayoutPanel4.Margin = new System.Windows.Forms.Padding(0);
			this.tableLayoutPanel4.Name = "tableLayoutPanel4";
			this.tableLayoutPanel4.RowCount = 1;
			this.tableLayoutPanel4.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
			this.tableLayoutPanel4.Size = new System.Drawing.Size(333, 30);
			this.tableLayoutPanel4.TabIndex = 3;
			// 
			// runsLabel
			// 
			this.runsLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.runsLabel.AutoSize = true;
			this.runsLabel.Location = new System.Drawing.Point(3, 8);
			this.runsLabel.Name = "runsLabel";
			this.runsLabel.Size = new System.Drawing.Size(105, 13);
			this.runsLabel.TabIndex = 0;
			this.runsLabel.Text = "Runs:";
			// 
			// errorsLabel
			// 
			this.errorsLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.errorsLabel.AutoSize = true;
			this.errorsLabel.Location = new System.Drawing.Point(114, 8);
			this.errorsLabel.Name = "errorsLabel";
			this.errorsLabel.Size = new System.Drawing.Size(105, 13);
			this.errorsLabel.TabIndex = 1;
			this.errorsLabel.Text = "Errors:";
			// 
			// failuresLabel
			// 
			this.failuresLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.failuresLabel.AutoSize = true;
			this.failuresLabel.Location = new System.Drawing.Point(225, 8);
			this.failuresLabel.Name = "failuresLabel";
			this.failuresLabel.Size = new System.Drawing.Size(105, 13);
			this.failuresLabel.TabIndex = 2;
			this.failuresLabel.Text = "Failures:";
			// 
			// toolStripImages
			// 
			this.toolStripImages.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("toolStripImages.ImageStream")));
			this.toolStripImages.TransparentColor = System.Drawing.Color.Transparent;
			this.toolStripImages.Images.SetKeyName(0, "Hierarchy");
			this.toolStripImages.Images.SetKeyName(1, "Memory");
			// 
			// testResultsProgress
			// 
			this.testResultsProgress.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
			this.testResultsProgress.Location = new System.Drawing.Point(333, 5);
			this.testResultsProgress.Margin = new System.Windows.Forms.Padding(0);
			this.testResultsProgress.Name = "testResultsProgress";
			this.testResultsProgress.Size = new System.Drawing.Size(334, 20);
			this.testResultsProgress.TabIndex = 2;
			// 
			// CxxTestResultsToolWindowPanel
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.tableLayoutPanel2);
			this.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.Name = "CxxTestResultsToolWindowPanel";
			this.Size = new System.Drawing.Size(667, 195);
			this.tableLayoutPanel2.ResumeLayout(false);
			this.splitContainer2.Panel1.ResumeLayout(false);
			this.splitContainer2.Panel1.PerformLayout();
			this.splitContainer2.Panel2.ResumeLayout(false);
			this.splitContainer2.Panel2.PerformLayout();
			this.splitContainer2.ResumeLayout(false);
			this.masterToolStrip.ResumeLayout(false);
			this.masterToolStrip.PerformLayout();
			this.detailsToolStrip.ResumeLayout(false);
			this.detailsToolStrip.PerformLayout();
			this.tableLayoutPanel3.ResumeLayout(false);
			this.tableLayoutPanel4.ResumeLayout(false);
			this.tableLayoutPanel4.PerformLayout();
			this.ResumeLayout(false);

        }

        #endregion

		private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
		private System.Windows.Forms.ColumnHeader columnHeader3;
		private WebCAT.CxxTest.VisualStudio.Controls.PassFailBar testResultsProgress;
		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
		private System.Windows.Forms.SplitContainer splitContainer2;
		private System.Windows.Forms.ToolStrip masterToolStrip;
		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel3;
		private System.Windows.Forms.TableLayoutPanel tableLayoutPanel4;
		private System.Windows.Forms.ToolStrip detailsToolStrip;
		private System.Windows.Forms.ToolStripLabel toolStripLabel1;
		private System.Windows.Forms.Label runsLabel;
		private System.Windows.Forms.Label errorsLabel;
		private System.Windows.Forms.Label failuresLabel;
		private System.Windows.Forms.ToolStripButton hierarchyButton;
		private System.Windows.Forms.ToolStripButton memoryButton;
		private System.Windows.Forms.ImageList masterDetailImages;
		private System.Windows.Forms.ImageList toolStripImages;
		private System.Windows.Forms.TreeView masterView;
		private System.Windows.Forms.TreeView detailsView;

    }
}
