/*==========================================================================*\
 |  $Id: HierarchyItemDialog.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using WebCAT.CxxTest.VisualStudio.Utility;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.VCProject;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	// --------------------------------------------------------------------
	/// <summary>
	/// A form that lets the user choose an item from a Visual Studio
	/// hierarchy. The items displayed and user's allowable selection can be
	/// filtered based on their type.
	/// </summary>
	public partial class HierarchyItemDialog : Form
	{
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new hierarchy item dialog.
		/// </summary>
		/// <param name="provider">
		/// The <code>IServiceProvider</code> to use to access Visual Studio
		/// services.
		/// </param>
		public HierarchyItemDialog(IServiceProvider provider)
		{
			InitializeComponent();

			serviceProvider = provider;
			visibleTypeGuids = null;
			selectableTypeGuids = null;
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets an array of GUIDs representing the types of hierarchy
		/// items that will be displayed in the dialog.
		/// </summary>
		/// <remarks>
		/// If null, no filtering will be performed; all items in the
		/// hierarchy will be visible.
		/// </remarks>
		public Guid[] VisibleTypeGuids
		{
			get
			{
				return visibleTypeGuids;
			}
			set
			{
				visibleTypeGuids = value;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets an array of GUIDs representing the types of hierarchy
		/// items that the user is allowed to select in the dialog.
		/// </summary>
		/// <remarks>
		/// If null, no filtering will be performed; all items in the
		/// hierarchy will be selectable by the user.
		/// </remarks>
		public Guid[] SelectableTypeGuids
		{
			get
			{
				return selectableTypeGuids;
			}
			set
			{
				selectableTypeGuids = value;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets the hierarchy item that was selected by the user.
		/// </summary>
		internal HierarchyItem SelectedItem
		{
			get
			{
				return selectedItem;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the message that will be displayed at the top of the
		/// dialog.
		/// </summary>
		public string Message
		{
			get
			{
				return messageLabel.Text;
			}
			set
			{
				messageLabel.Text = value;
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Called when the form is loaded.
		/// </summary>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		protected override void OnLoad(EventArgs e)
		{
			base.OnLoad(e);

			// Recursively add each project's hierarchy items to the tree.

			foreach (HierarchyItem item in
				VsShellUtils.GetLoadedProjects(serviceProvider))
			{
				AddNode(item, hierarchyItemTree.Nodes);
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called just before the form is closed.
		/// </summary>
		/// <param name="e">
		/// A <code>CancelEventArgs</code> object.
		/// </param>
		protected override void OnClosing(CancelEventArgs e)
		{
			base.OnClosing(e);

			// Keep track of the user's selection if they verified it by
			// pressing OK.

			if (DialogResult == DialogResult.OK)
			{
				selectedItem =
					(HierarchyItem)hierarchyItemTree.SelectedNode.Tag;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when a node in the tree is selected.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// A <code>TreeViewEventArgs</code> object.
		/// </param>
		private void hierarchyItemTree_AfterSelect(object sender,
			TreeViewEventArgs e)
		{
			HierarchyItem item = (HierarchyItem)e.Node.Tag;

			// Check the type of the selected item against the selectable type
			// GUIDs and enable/disable the OK button accordingly.

			Guid typeGuid = item.TypeGuid;
			bool enabled = true;

			if (selectableTypeGuids != null &&
				Array.IndexOf(selectableTypeGuids, typeGuid) == -1)
				enabled = false;

			okButton.Enabled = enabled;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Adds a tree node for the specified hierarchy item to a tree node
		/// collection.
		/// </summary>
		/// <param name="item">
		/// A hierarchy item.
		/// </param>
		/// <param name="coll">
		/// The <code>TreeNodeCollection</code> to add the new node to.
		/// </param>
		private void AddNode(HierarchyItem item,
			TreeNodeCollection coll)
		{
			// Skip the item if it is not one of the visible types.

			Guid typeGuid = item.TypeGuid;

			if (visibleTypeGuids != null &&
				Array.IndexOf(visibleTypeGuids, typeGuid) == -1)
				return;

			// Add the item's icon to the end of the image list.

			imageList.Images.Add(item.Icon);
			int imageIndex = imageList.Images.Count - 1;

			TreeNode node = new TreeNode();
			node.Text = item.Caption;
			node.ImageIndex = node.SelectedImageIndex = imageIndex;
			node.Tag = item;

			coll.Add(node);

			// Recurse through the item's children.

			foreach (HierarchyItem child in item.Children)
				AddNode(child, node.Nodes);

			node.Expand();
		}


		//~ Instance variables ...............................................

		// The service provider used to access Visual Studio services.
		private IServiceProvider serviceProvider;

		// The GUIDs representing hierarchy item types that will be visible in
		// the form.
		private Guid[] visibleTypeGuids;

		// The GUIDs representing hierarchy item types that the user can
		// select in the form.
		private Guid[] selectableTypeGuids;

		// The hierarchy item that the user selected.
		private HierarchyItem selectedItem;
	}
}
