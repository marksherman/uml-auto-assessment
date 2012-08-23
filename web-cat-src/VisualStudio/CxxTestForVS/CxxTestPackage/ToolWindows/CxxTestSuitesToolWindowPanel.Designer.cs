/*==========================================================================*\
 |  $Id: CxxTestSuitesToolWindowPanel.Designer.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
    partial class CxxTestSuitesToolWindowPanel
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        protected override void Dispose( bool disposing )
        {
            if( disposing )
            {
                if(components != null)
                {
                    components.Dispose();
                }
            }
            base.Dispose( disposing );
        }


        #region Component Designer generated code
        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
			this.components = new System.ComponentModel.Container();
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CxxTestSuitesToolWindowPanel));
			this.testsTreeView = new System.Windows.Forms.TreeView();
			this.imageList = new System.Windows.Forms.ImageList(this.components);
			this.SuspendLayout();
			// 
			// testsTreeView
			// 
			this.testsTreeView.CheckBoxes = true;
			this.testsTreeView.Dock = System.Windows.Forms.DockStyle.Fill;
			this.testsTreeView.ImageIndex = 0;
			this.testsTreeView.ImageList = this.imageList;
			this.testsTreeView.Indent = 23;
			this.testsTreeView.Location = new System.Drawing.Point(0, 0);
			this.testsTreeView.Margin = new System.Windows.Forms.Padding(0);
			this.testsTreeView.Name = "testsTreeView";
			this.testsTreeView.SelectedImageIndex = 0;
			this.testsTreeView.ShowRootLines = false;
			this.testsTreeView.Size = new System.Drawing.Size(204, 157);
			this.testsTreeView.TabIndex = 0;
			this.testsTreeView.NodeMouseDoubleClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.testsTreeView_NodeMouseDoubleClick);
			this.testsTreeView.AfterCheck += new System.Windows.Forms.TreeViewEventHandler(this.testsTreeView_AfterCheck);
			// 
			// imageList
			// 
			this.imageList.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList.ImageStream")));
			this.imageList.TransparentColor = System.Drawing.Color.Magenta;
			this.imageList.Images.SetKeyName(0, "Solution");
			this.imageList.Images.SetKeyName(1, "TestSuite");
			this.imageList.Images.SetKeyName(2, "TestCase");
			// 
			// CxxTestSuitesToolWindowPanel
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.testsTreeView);
			this.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.Name = "CxxTestSuitesToolWindowPanel";
			this.Size = new System.Drawing.Size(204, 157);
			this.ResumeLayout(false);

        }
        #endregion

		private System.Windows.Forms.ImageList imageList;
		private System.Windows.Forms.TreeView testsTreeView;


    }
}
