/*==========================================================================*\
 |  $Id: TargetTreeNode.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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
using System.Text;
using System.Windows.Forms;
using WebCAT.Submitter;

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// A TreeNode that wraps a submission target.
	/// </summary>
	class TargetTreeNode : TreeNode
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new tree node that wraps the specified target.
		/// </summary>
		/// <remarks>
		/// The new tree node automatically obtains its text representation
		/// from the name of the target, if it has one.
		/// </remarks>
		/// <param name="target">
		/// The target to be wrapped.
		/// </param>
		public TargetTreeNode(ITarget target)
		{
			this.target = target;

			INameableTarget nameable = target as INameableTarget;

			if(nameable != null)
			{
				this.Text = nameable.Name;
			}

			if (target.IsContainer)
			{
				PlaceholderTreeNode node = new PlaceholderTreeNode();
				this.Nodes.Add(node);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the submission target wrapped by this tree node.
		/// </summary>
		public ITarget Target
		{
			get
			{
				return target;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Populates this node's children if they have not already been
		/// created.
		/// </summary>
		/// <param name="load">
		/// True if this should also cause remote imported groups to be loaded.
		/// </param>
		public void PopulateChildren(bool load)
		{
			if (this.FirstNode is PlaceholderTreeNode)
			{
				this.FirstNode.Remove();

				List<ITarget> children = new List<ITarget>();

				ComputeImmediateChildren(target, children, load);

				foreach (ITarget child in children)
				{
					TargetTreeNode childNode = new TargetTreeNode(child);
					this.Nodes.Add(childNode);
				}
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Computes the immediate children of the given submission target.
		/// </summary>
		/// <remarks>
		/// The logic in this method is not as simple as accessing the Children
		/// property of the target because the user interface does not display
		/// the exact hierarchy of the targets as defined in the XML file. For
		/// example, assignment-groups that are not given names are used only
		/// for grouping of properties (such as files included and excluded),
		/// and thus the assignments inside them are displayed at the same
		/// level as the group itself. The IsNested property of the target
		/// determines this.
		/// </remarks>
		/// <param name="parent">
		/// The submission target whose children should be computed.
		/// </param>
		/// <param name="list">
		/// A list that will contain all of the children of the target upon
		/// returning.
		/// </param>
		/// <param name="load">
		/// True if this should also cause remote imported groups to be loaded.
		/// </param>
		private void ComputeImmediateChildren(ITarget parent,
			List<ITarget> list, bool load)
		{
			if (parent.IsLoaded || load)
			{
				foreach (ITarget target in parent.Children)
				{
					IHideableTarget hideable = target as IHideableTarget;

					if (hideable == null ||
						(hideable != null && !hideable.Hidden))
					{
						if (target.IsContainer && !target.IsNested)
						{
							ComputeImmediateChildren(target, list, load);
						}
						else
						{
							list.Add(target);
						}
					}
				}
			}
		}

		// The submission target being wrapped.
		private ITarget target;
	}
}
