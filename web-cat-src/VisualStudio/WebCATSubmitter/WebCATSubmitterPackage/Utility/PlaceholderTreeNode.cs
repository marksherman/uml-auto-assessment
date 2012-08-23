/*==========================================================================*\
 |  $Id: PlaceholderTreeNode.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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

namespace WebCAT.Submitter.VisualStudio.Utility
{
	/// <summary>
	/// This TreeNode subclass does not add any functionality, but exists as
	/// a distinct type that is added to unexpanded nodes in the submission
	/// wizard before they are first expanded. Once expanded, the placeholder
	/// is removed and replaced with the actual children of the node.
	/// </summary>
	class PlaceholderTreeNode : TreeNode
	{
	}
}
