/*==========================================================================*\
 |  $Id: ITargetImportGroup.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission engine for the
 |	.NET framework.
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

namespace WebCAT.Submitter
{
	/// <summary>
	/// The interface that represents an imported group in the submission
	/// target tree. An imported group refers to an external XML submission
	/// target file that will be merged with the tree at the location of the
	/// import group node. The imported group must have a name and a valid
	/// URL to the external file.
	/// </summary>
	public interface ITargetImportGroup :
		ITarget, INameableTarget, IHideableTarget
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a string containing the URL to the external submission targets
		/// file that will be imported into this group.
		/// </summary>
		string Href
		{
			get;
			set;
		}
	}
}
