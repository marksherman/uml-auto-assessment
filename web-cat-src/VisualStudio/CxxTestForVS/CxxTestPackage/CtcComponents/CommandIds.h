/*==========================================================================*\
 |  $Id: CommandIds.h,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

// CommandIds.h
// Command IDs used in defining command bars
//

// do not use #pragma once - used by ctc compiler
#ifndef __COMMANDIDS_H_
#define __COMMANDIDS_H_

///////////////////////////////////////////////////////////////////////////////
// Menu IDs

#define CxxTestViewToolbar				0x1000


///////////////////////////////////////////////////////////////////////////////
// Menu Group IDs

#define SolnExplorerItemGroup			0x1020
#define CxxTestViewToolbarGroup			0x1021


///////////////////////////////////////////////////////////////////////////////
// Command IDs


#define cmdidCxxTestViewWindow			0x101
#define cmdidCxxTestResultsWindow		0x201
#define cmdidGenerateTests				0x301

#define cmdidSelectAllTests				0x401
#define cmdidRefreshTests				0x402
#define cmdidRunTests					0x403

///////////////////////////////////////////////////////////////////////////////
// Bitmap IDs

#define bmpCxxTestView					1
#define bmpCxxTestResultsView			2
#define bmpGenerateTestSuite			3
#define bmpRunTests						4
#define bmpRefreshTests					5


#endif // __COMMANDIDS_H_
