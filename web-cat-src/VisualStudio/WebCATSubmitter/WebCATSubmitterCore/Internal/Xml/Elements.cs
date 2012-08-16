/*==========================================================================*\
 |  $Id: Elements.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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

namespace WebCAT.Submitter.Internal.Xml
{
	/// <summary>
	/// This class contains string constants that represent the names of the
	/// XML elements used in the submission definitions XML file. Referring to
	/// them symbolicly provides better error checking and lets us easily use
	/// refactoring if we need to make a change to an element name in the
	/// future.
	/// </summary>
	static class Elements
	{
		public const string SubmissionTargets = "submission-targets";

		public const string Assignment = "assignment";

		public const string AssignmentGroup = "assignment-group";

		public const string ImportGroup = "import-group";

		public const string Transport = "transport";

		public const string Packager = "packager";

		public const string Param = "param";

		public const string FileParam = "file-param";

		public const string Include = "include";

		public const string Exclude = "exclude";

		public const string Required = "required";

		public const string FilterAmbiguity = "filter-ambiguity";
	}
}
