/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
#ifndef __SYMREADER_H__
#define __SYMREADER_H__

/**
 *  symreader.h
 *
 *  Created by Tony Allevato on 7/23/06.
 *
 *  Interface for the symbol reader. Since BFD is incomplete on MacOS X, we use
 *  a common interface with an appropriate implementation on OS X (using Mach-O
 *  Objective-C classes), and using BFD on Windows/Linux.
 */

/**
 * The maximum length of the buffer that will hold the name of a symbol.
 */
#define SYMNAME_MAX 512

#define SYMFLAGS_DEMANGLE 1

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Initializes the symbol reader with the specified executable filename.
 *
 * @param path the path to the executable whose symbols will be read
 * @param flags flags indicating behavior of the symbol reader
 * @returns non-zero if the initialization was successful; otherwise, zero.
 */
extern int symreader_initialize(const char* path, int flags);

extern int symreader_is_initialized();

/**
 * Gets information about a symbol from the executable's symbol table.
 *
 * @param address the address of the symbol we wish to find
 * @param symName a char buffer that will store the name of the symbol
 * @param filename a char buffer that will store the name of the source file
 *     containing the symbol
 * @param line a pointer to an integer that will hold the line number of the
 *     symbol in the source file
 * @returns the true address of the symbol; that is, the address at which the
 *     symbol actually begins
 */
extern void* symreader_get_symbol_info(void* address, char* symName, char* filename, int* line);

/**
 * Cleans up data created by the symbol reader during initialization.
 */
extern void symreader_cleanup();

#ifdef __cplusplus
}
#endif

#endif // __SYMREADER_H__
/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
#ifndef __SYMREADER_H__
#define __SYMREADER_H__

/**
 *  symreader.h
 *
 *  Created by Tony Allevato on 7/23/06.
 *
 *  Interface for the symbol reader. Since BFD is incomplete on MacOS X, we use
 *  a common interface with an appropriate implementation on OS X (using Mach-O
 *  Objective-C classes), and using BFD on Windows/Linux.
 */

/**
 * The maximum length of the buffer that will hold the name of a symbol.
 */
#define SYMNAME_MAX 512

#define SYMFLAGS_DEMANGLE 1

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Initializes the symbol reader with the specified executable filename.
 *
 * @param path the path to the executable whose symbols will be read
 * @param flags flags indicating behavior of the symbol reader
 * @returns non-zero if the initialization was successful; otherwise, zero.
 */
extern int symreader_initialize(const char* path, int flags);

/**
 * Returns non-zero if the symbol reader library has been initialized.
 * 
 * @returns a non-zero value if initialized; otherwise, zero.
 */
extern int symreader_is_initialized();

/**
 * Gets information about a symbol from the executable's symbol table.
 *
 * @param address the address of the symbol we wish to find
 * @param symName a char buffer that will store the name of the symbol
 * @param filename a char buffer that will store the name of the source file
 *     containing the symbol
 * @param line a pointer to an integer that will hold the line number of the
 *     symbol in the source file
 * @returns the true address of the symbol; that is, the address at which the
 *     symbol actually begins
 */
extern void* symreader_get_symbol_info(void* address, char* symName, char* filename, int* line);

/**
 * Cleans up data created by the symbol reader during initialization.
 */
extern void symreader_cleanup();

#ifdef __cplusplus
}
#endif

#endif // __SYMREADER_H__
