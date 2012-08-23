/*==========================================================================*\
 |  $Id: ChooseProjectsToSubmitDialog.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.IO;
using WebCAT.Submitter.VisualStudio.Submittables;
using WebCAT.Submitter.VisualStudio.Utility;
using Microsoft.VisualStudio.Shell.Interop;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio;

namespace WebCAT.Submitter.VisualStudio.Forms
{
	/// <summary>
	/// A dialog that allows the user to choose a set of projects.
	/// </summary>
	public partial class ChooseProjectsToSubmitDialog : Form
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the choose projects dialog.
		/// </summary>
		/// <param name="dte">
		/// The DTE object to use to access Visual Studio services.
		/// </param>
		/// <param name="selection">
		/// The initial selection that should be displayed in the dialog.
		/// </param>
		public ChooseProjectsToSubmitDialog(IServiceProvider sp,
			ISubmittableItem[] selection)
		{
			this.serviceProvider = sp;
			this.selection = selection;

			InitializeComponent();

			PopulateTree();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the selection that was made in the dialog.
		/// </summary>
		public ISubmittableItem[] Selection
		{
			get
			{
				List<ISubmittableItem> items = new List<ISubmittableItem>();
				
				foreach(TreeNode node in treeView.Nodes)
					AppendSelectionFromNode(items, node);

				return items.ToArray();
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Appends to a selection list the submittable item in the given node,
		/// if it is checked.
		/// </summary>
		/// <remarks>
		/// This method handles nested elements -- if the parent of an item is
		/// checked, that means that all of its children are as well, so only
		/// the parent needs to be added to the selection. If a parent node is
		/// not checked, then we recursively look at its children to determine
		/// if any of them need to be added to the selection.
		/// </remarks>
		/// <param name="items">
		/// The list that will be built up to contain the selection.
		/// </param>
		/// <param name="node">
		/// The node to possibly add to the selection.
		/// </param>
		private void AppendSelectionFromNode(List<ISubmittableItem> items,
			TreeNode node)
		{
			if (node.Checked)
			{
				string solutionDir, solutionFile, solutionUser;
				IVsSolution solution = VsShellUtils.GetSolution(serviceProvider);
				solution.GetSolutionInfo(out solutionDir, out solutionFile,
					out solutionUser);

				ISubmittableItem item = null;

				if (node.Tag is IVsSolution)
				{
					item = new SubmittableSolution((IVsSolution)node.Tag);
				}
				else if (node.Tag is HierarchyItem)
				{
					HierarchyItem hierarchy = (HierarchyItem)node.Tag;

					item = new SubmittableProject(solutionDir, hierarchy);
				}

				if(item != null)
				{
					items.Add(item);
				}
			}
			else
			{
				foreach (TreeNode child in node.Nodes)
				{
					AppendSelectionFromNode(items, child);
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Populates the tree with the current solution and the projects
		/// loaded in the solution.
		/// </summary>
		private void PopulateTree()
		{
			string solutionDir, solutionFile, solutionUser;
			IVsSolution solution = VsShellUtils.GetSolution(serviceProvider);
			solution.GetSolutionInfo(out solutionDir, out solutionFile,
				out solutionUser);

			string solutionName =
				Path.GetFileNameWithoutExtension(solutionFile);

			TreeNode root = new TreeNode(
				String.Format(Messages.SolutionPlaceholder, solutionName));

			root.Tag = solution;
			treeView.Nodes.Add(root);

			if (FindSolutionInSelection(solution))
				root.Checked = true;

			foreach (HierarchyItem item in
				VsShellUtils.GetLoadedProjects(solution))
			{
				AddHierarchyItemToNode(item, root);
			}

			treeView.ExpandAll();
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Adds the specified hierarchy item as a new child node of the given
		/// node.
		/// </summary>
		/// <param name="item">
		/// The hierarchy item to be added.
		/// </param>
		/// <param name="parent">
		/// The node to add this hierarchy item as a child of.
		/// </param>
		private void AddHierarchyItemToNode(HierarchyItem item,
			TreeNode parent)
		{
			TreeNode node = new TreeNode(item["Name"].ToString());
			node.Tag = item;

			treeImages.Images.Add(item.Icon);

			node.ImageIndex = node.SelectedImageIndex =
				treeImages.Images.Count - 1;

			if (parent.Checked || FindHierarchyItemInSelection(item))
				node.Checked = true;

			parent.Nodes.Add(node);

			// Perhaps recursively handle nested projects here?
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Handles when the user checks or unchecks a node by fixing the
		/// check states of its parent and descendants.
		/// </summary>
		/// <param name="sender">
		/// The tree view that sent the event.
		/// </param>
		/// <param name="e">
		/// The event arguments for the event.
		/// </param>
		private void treeView_AfterCheck(object sender, TreeViewEventArgs e)
		{
			if (!fixingChecks)
			{
				fixingChecks = true;
				FixChecksDownward(e.Node);
				FixChecksUpward(e.Node.Parent);
				fixingChecks = false;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Searches the current selection to determine if the given solution
		/// is contained in it.
		/// </summary>
		/// <param name="solution">
		/// The solution to search for.
		/// </param>
		/// <returns>
		/// True if the solution was found in the selection; otherwise, false.
		/// </returns>
		private bool FindSolutionInSelection(IVsSolution solution)
		{
			foreach (ISubmittableItem item in selection)
			{
				SubmittableSolution ss = item as SubmittableSolution;

				if (ss != null)
				{
					if (ss.Solution == solution)
					{
						return true;
					}
				}
			}

			return false;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Searches the current selection to determine if the given hierarchy
		/// item is contained in it.
		/// </summary>
		/// <param name="hierarchyItem">
		/// The hierarchy item to search for.
		/// </param>
		/// <returns>
		/// True if the hierarchy item was found in the selection; otherwise,
		/// false.
		/// </returns>
		private bool FindHierarchyItemInSelection(HierarchyItem hierarchyItem)
		{
			foreach (ISubmittableItem item in selection)
			{
				SubmittableProject sp = item as SubmittableProject;

				if (sp != null)
				{
					if (sp.HierarchyItem.Equals(hierarchyItem))
					{
						return true;
					}
				}
			}

			return false;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when a node is checked or unchecked to update the check
		/// states of the node's descendants.
		/// </summary>
		/// <param name="node">
		/// The node whose descendants need to be updated.
		/// </param>
		private void FixChecksDownward(TreeNode node)
		{
			foreach (TreeNode child in node.Nodes)
			{
				child.Checked = node.Checked;
				FixChecksDownward(child);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when a node is checked or unchecked to update the check
		/// states of the node's ancestors.
		/// </summary>
		/// <param name="node">
		/// The node whose ancestors need to be updated.
		/// </param>
		private void FixChecksUpward(TreeNode node)
		{
			if (node == null)
				return;

			bool allChecked = true;

			foreach (TreeNode child in node.Nodes)
			{
				if (child.Checked == false)
				{
					allChecked = false;
					break;
				}
			}

			node.Checked = allChecked;

			FixChecksUpward(node.Parent);
		}

		// The DTE object used to access other Visual Studio services.
		private IServiceProvider serviceProvider;

		// The currently selected projects or solution.
		private ISubmittableItem[] selection;

		// Used to prevent AfterCheck events from being processed while the
		// tree checkboxes are being updated recursively.
		private bool fixingChecks;
	}
}