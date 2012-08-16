/*==========================================================================*\
 |  $Id: Guids.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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

namespace WebCAT.Submitter.VisualStudio
{
	/// <summary>
	/// Constants that identify the GUIDs used in this package. These values
	/// must correspond to the values in Guids.h that are used when processing
	/// the CTC table.
	/// </summary>
	internal static class Guids
	{
		/// <summary>
		/// The GUID that identifies this package in Visual Studio.
		/// </summary>
		public const string WebCATSubmitterPackageString =
			"5df72b95-66ab-4a47-87c2-02d9da3a8cd8";

		/// <summary>
		/// The GUID that identifies this package in Visual Studio.
		/// </summary>
		public static readonly Guid WebCATSubmitterPackage =
			new Guid(WebCATSubmitterPackageString);

		/// -------------------------------------------------------------------

		/// <summary>
		/// The GUID that identifies the command set defined by this package.
		/// </summary>
		public const string WebCATSubmitterPackageCmdSetString =
			"76966484-5a24-4942-9cde-dbdea79acf30";

		/// <summary>
		/// The GUID that identifies the command set defined by this package.
		/// </summary>
		public static readonly Guid WebCATSubmitterPackageCmdSet =
			new Guid(WebCATSubmitterPackageCmdSetString);
    }
}
