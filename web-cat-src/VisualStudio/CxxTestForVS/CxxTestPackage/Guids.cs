/*==========================================================================*\
 |  $Id: Guids.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Various GUIDs used by this package. The values here must match those
	/// in CtcComponents/Guids.h.
	/// </summary>
    internal static class Guids
    {
        public const string CxxTestPackageString =
			"bfacf265-d997-495d-ba55-adbd35dc92fa";
        public const string CxxTestPackageCmdSetString =
			"2372b712-1653-4908-82bb-3a9d417298db";
        public const string TestSuitesToolWindowPersistanceString =
			"c2241c57-a91f-48d4-a532-6fe43891e006";
        public const string TestResultsToolWindowPersistanceString =
			"74c97db6-1ad5-4215-a40e-c579d218ff27";

        public static readonly Guid CxxTestPackage =
			new Guid(CxxTestPackageString);
        public static readonly Guid CxxTestPackageCmdSet =
			new Guid(CxxTestPackageCmdSetString);
        public static readonly Guid TestSuitesToolWindowPersistence =
			new Guid(TestSuitesToolWindowPersistanceString);
        public static readonly Guid TestResultsToolWindowPersistance =
			new Guid(TestResultsToolWindowPersistanceString);
    }
}