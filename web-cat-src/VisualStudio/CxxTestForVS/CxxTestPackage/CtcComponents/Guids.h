/*==========================================================================*\
 |  $Id: Guids.h,v 1.1 2008/06/02 23:27:38 aallowat Exp $
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

// guids.h: definitions of GUIDs/IIDs/CLSIDs used in this VsPackage

/*
Do not use #pragma once, as this file needs to be included twice.  Once to declare the externs
for the GUIDs, and again right after including initguid.h to actually define the GUIDs.
*/


// guidPersistanceSlot ID for the Tool Window
// { c2241c57-a91f-48d4-a532-6fe43891e006 }
#define guidTestSuitesToolWindowPersistanceSlot { 0xC2241C57, 0xA91F, 0x48D4, { 0xA5, 0x32, 0x6F, 0xE4, 0x38, 0x91, 0xE0, 0x6 } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_guidTestSuitesToolWindowPersistanceSlot, 
0xC2241C57, 0xA91F, 0x48D4, 0xA5, 0x32, 0x6F, 0xE4, 0x38, 0x91, 0xE0, 0x6 );
#endif

// {74C97DB6-1AD5-4215-A40E-C579D218FF27}
#define guidTestResultsToolWindowPersistanceSlot {0x74c97db6, 0x1ad5, 0x4215, {0xa4, 0xe, 0xc5, 0x79, 0xd2, 0x18, 0xff, 0x27}}
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_guidTestResultsToolWindowPersistanceSlot,
0x74c97db6, 0x1ad5, 0x4215, 0xa4, 0xe, 0xc5, 0x79, 0xd2, 0x18, 0xff, 0x27);
#endif


// package guid
// { bfacf265-d997-495d-ba55-adbd35dc92fa }
#define guidCxxTestPackage { 0xBFACF265, 0xD997, 0x495D, { 0xBA, 0x55, 0xAD, 0xBD, 0x35, 0xDC, 0x92, 0xFA } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_CxxTestPackage,
0xBFACF265, 0xD997, 0x495D, 0xBA, 0x55, 0xAD, 0xBD, 0x35, 0xDC, 0x92, 0xFA );
#endif

// Command set guid for our commands (used with IOleCommandTarget)
// { 2372b712-1653-4908-82bb-3a9d417298db }
#define guidCxxTestPackageCmdSet { 0x2372B712, 0x1653, 0x4908, { 0x82, 0xBB, 0x3A, 0x9D, 0x41, 0x72, 0x98, 0xDB } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_CxxTestPackageCmdSet, 
0x2372B712, 0x1653, 0x4908, 0x82, 0xBB, 0x3A, 0x9D, 0x41, 0x72, 0x98, 0xDB );
#endif

// Visibility context GUID for when multiple files are selected in the
// Solution Explorer
// {B899823D-A872-4c69-9DDD-4F5E4109006E}
#define guidMultipleFilesSelected { 0xb899823d, 0xa872, 0x4c69, { 0x9d, 0xdd, 0x4f, 0x5e, 0x41, 0x9, 0x0, 0x6e } }
#ifdef DEFINE_GUID
DEFINE_GUID(CLSID_MultipleFilesSelected, 
0xb899823d, 0xa872, 0x4c69, 0x9d, 0xdd, 0x4f, 0x5e, 0x41, 0x9, 0x0, 0x6e);
#endif
