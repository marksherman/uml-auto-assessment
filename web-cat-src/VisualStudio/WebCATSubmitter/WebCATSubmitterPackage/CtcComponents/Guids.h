/*==========================================================================*\
 |  $Id: Guids.h,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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


// guids.h: definitions of GUIDs/IIDs/CLSIDs used in this VsPackage

/*
Do not use #pragma once, as this file needs to be included twice.  Once to declare the externs
for the GUIDs, and again right after including initguid.h to actually define the GUIDs.
*/



// package guid
// { 5df72b95-66ab-4a47-87c2-02d9da3a8cd8 }
#define guidWebCATSubmitterPackage { 0x5DF72B95, 0x66AB, 0x4A47, { 0x87, 0xC2, 0x2, 0xD9, 0xDA, 0x3A, 0x8C, 0xD8 } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_WebCATSubmitterPackage,
0x5DF72B95, 0x66AB, 0x4A47, 0x87, 0xC2, 0x2, 0xD9, 0xDA, 0x3A, 0x8C, 0xD8 );
#endif

// Command set guid for our commands (used with IOleCommandTarget)
// { 76966484-5a24-4942-9cde-dbdea79acf30 }
#define guidWebCATSubmitterPackageCmdSet { 0x76966484, 0x5A24, 0x4942, { 0x9C, 0xDE, 0xDB, 0xDE, 0xA7, 0x9A, 0xCF, 0x30 } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_WebCATSubmitterPackageCmdSet, 
0x76966484, 0x5A24, 0x4942, 0x9C, 0xDE, 0xDB, 0xDE, 0xA7, 0x9A, 0xCF, 0x30 );
#endif
