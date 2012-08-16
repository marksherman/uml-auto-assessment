/*==========================================================================*\
 |  $Id: CommandIds.h,v 1.2 2008/12/12 01:44:09 aallowat Exp $
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


// CommandIds.h
// Command IDs used in defining command bars
//

// do not use #pragma once - used by ctc compiler
#ifndef __COMMANDIDS_H_
#define __COMMANDIDS_H_

///////////////////////////////////////////////////////////////////////////////
// Menu IDs

///////////////////////////////////////////////////////////////////////////////
// Menu Group IDs

#define SubmitSolutionGroup                 0x1020
#define SubmitProjectGroup                  0x1021

///////////////////////////////////////////////////////////////////////////////
// Command IDs

#define cmdidSubmitSolution 0x100
#define cmdidSubmitProject  0x101

///////////////////////////////////////////////////////////////////////////////
// Bitmap IDs

#define bmpPicSubmit 1
#define bmpPic2 2
#define bmpPic3 3
#define bmpPic4 4
#define bmpPic5 5

#endif // __COMMANDIDS_H_
