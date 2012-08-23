/*==========================================================================*\
 |  $Id: NewCxxTestSuiteWizardPage2.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using WebCAT.CxxTest.VisualStudio.Utility;
using Microsoft.VisualStudio.VCCodeModel;
using EnvDTE;
using stdole;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	public partial class NewCxxTestSuiteWizardPage2 : WizardPageControl
	{
		public NewCxxTestSuiteWizardPage2(IServiceProvider sp)
		{
			InitializeComponent();

			serviceProvider = sp;
		}

		internal void PopulateWithHeader(HierarchyItem headerUnderTest)
		{
			functionsTree.Nodes.Clear();

			ProjectItem projectItem = headerUnderTest.GetExtObjectAs<ProjectItem>();
			VCFileCodeModel codeModel = projectItem.FileCodeModel as VCFileCodeModel;

			PopulateRecursively(functionsTree.Nodes, codeModel.CodeElements);

			functionsTree.ExpandAll();
		}

		private void PopulateRecursively(TreeNodeCollection nodes, CodeElements elements)
		{
			for (int i = 1; i <= elements.Count; i++)
			{
				VCCodeElement element = elements.Item(i) as VCCodeElement;

				if (IsElementAcceptableType(element))
				{
					TreeNode node = new TreeNode();
					node.Text = element.Name;
					node.Tag = element;

					IPicture picture = (IPicture) element.Picture;
					Icon icon = Icon.FromHandle(new IntPtr(picture.Handle));

					imageList.Images.Add(icon);
					node.ImageIndex = node.SelectedImageIndex = imageList.Images.Count - 1;
					node.Checked = true;

					nodes.Add(node);

					PopulateRecursively(node.Nodes, element.Children);
				}
			}
		}

		private bool IsElementAcceptableType(VCCodeElement element)
		{
			switch (element.Kind)
			{
				case vsCMElement.vsCMElementFunction:
					VCCodeFunction function = (VCCodeFunction)element;

					if (element.Name.StartsWith("~") ||
						function.Access != vsCMAccess.vsCMAccessPublic)
						return false;
					else
						return true;

				case vsCMElement.vsCMElementClass:
					VCCodeClass klass = (VCCodeClass)element;

					if (klass.Access != vsCMAccess.vsCMAccessPublic)
						return false;
					else
						return true;

				case vsCMElement.vsCMElementNamespace:
					return true;

				case vsCMElement.vsCMElementStruct:
					VCCodeStruct strukt = (VCCodeStruct)element;

					if (strukt.Access != vsCMAccess.vsCMAccessPublic)
						return false;
					else
						return true;

				default:
					return false;
			}
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

		private void functionsTree_AfterCheck(object sender, TreeViewEventArgs e)
		{
			if (!fixingChecks)
			{
				fixingChecks = true;
				FixChecksDownward(e.Node);
				FixChecksUpward(e.Node.Parent);
				fixingChecks = false;

				VCCodeFunction[] functions = SelectedFunctions;
				functionsSelectedLabel.Text =
					String.Format("{0} functions/methods selected.", functions.Length);
			}
		}

		public VCCodeFunction[] SelectedFunctions
		{
			get
			{
				List<VCCodeFunction> functions = new List<VCCodeFunction>();

				GetCheckedFunctionsRecursively(functions, functionsTree.Nodes);

				return functions.ToArray();
			}
		}

		private void GetCheckedFunctionsRecursively(List<VCCodeFunction> functions, TreeNodeCollection nodes)
		{
			foreach (TreeNode node in nodes)
			{
				if (node.Checked)
				{
					VCCodeElement element = (VCCodeElement)node.Tag;

					if (element is VCCodeFunction)
						functions.Add((VCCodeFunction)element);
				}

				GetCheckedFunctionsRecursively(functions, node.Nodes);
			}
		}

		private IServiceProvider serviceProvider;

		// Used to prevent AfterCheck events from being processed while the
		// tree checkboxes are being updated recursively.
		private bool fixingChecks;
	}
}
