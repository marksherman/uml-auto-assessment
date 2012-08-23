/*==========================================================================*\
 |  $Id: CommandIds.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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
	/// Command ID numbers used in this package. The constants here must match
	/// those defined in CtcComponents/CommandIds.h.
	/// </summary>
    internal static class CommandIds
    {
		public const int CxxTestViewToolbar			= 0x1000;

		public const int CxxTestViewToolbarGroup	= 0x1021;

        public const int cmdidCxxTestViewWindow		= 0x101;
        public const int cmdidCxxTestResultsWindow	= 0x201;
        public const int cmdidGenerateTests			= 0x301;

		public const int cmdidSelectAllTests		= 0x401;
		public const int cmdidRefreshTests			= 0x402;
		public const int cmdidRunTests				= 0x403;
	}
}