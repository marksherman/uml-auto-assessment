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

/*
 *  symreader.c
 */

#include "symreader.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <bfd.h>
/*#include <demangle.h>*/

extern "C"
{
	char* cplus_demangle(const char*, int);
}

static int initialized = 0;

static int symFlags;
static bfd* abfd = 0;
static asymbol** syms = 0;
static unsigned long numSymbols = 0;
static asymbol** symTable = 0;

typedef struct _SymbolInfo
{
	bfd_vma pc;
	const char* filename;
	const char* funcName;
	int line;
	int found;
} SymbolInfo;

static int verifyLoadedSymbolInfo()
{
	char** matching;

	if(!abfd)
	{
		fprintf(stderr, "Error loading symbols from executable image: %s\n",
			bfd_errmsg(bfd_get_error()));
		return 0;
	}

	if(bfd_check_format(abfd, bfd_archive))
	{
		fprintf(stderr, "Format error loading symbols from executable image: %s\n",
			bfd_errmsg(bfd_get_error()));
		return 0;
	}

	if(!bfd_check_format_matches(abfd, bfd_object, &matching))
	{
		fprintf(stderr, "Error loading symbols from executable image: "
			"format does not match\n");
		free(matching);
		return 0;
	}

	if(!(bfd_get_file_flags(abfd) & HAS_SYMS))
	{
		fprintf(stderr, "Error loading symbols from executable image: "
			"no symbols found\n");
		return 0;
	}

	unsigned int size;
	numSymbols = bfd_read_minisymbols(abfd, 0, (void**)&syms, &size);

	if(!numSymbols)
	{
		numSymbols = bfd_read_minisymbols(abfd, 1, (void**)&syms, &size);
	}

	// supporting dynamic symbols
	long storage = bfd_get_symtab_upper_bound(abfd);
	if(storage < 1)
	{
		fprintf(stderr, "Error loading symbols from executable image: "
			"no symbols found\n");
		return 0;
	}

	syms = (asymbol**)malloc(storage);
	numSymbols = bfd_canonicalize_symtab(abfd, syms);
	if(numSymbols < 1)
	{
		free(syms);
		fprintf(stderr, "Error loading symbols from executable image: "
			"no canonical symbols found\n");
		return 0;
	}

	return 1;
}

static void findBfdAddress(bfd* abfd, asection* section, void* data)
{
	SymbolInfo* info = (SymbolInfo*)data;
	if(info->found) return;
	if(!(bfd_get_section_flags(abfd, section) & SEC_ALLOC)) return;

	bfd_vma pc = info->pc;
	if(pc < section->vma) return;

	pc -= section->vma;
	if(pc >= section->size) return;

	info->found = bfd_find_nearest_line(abfd, section, syms, pc,
		&info->filename, &info->funcName, (unsigned int*)(&info->line));
}

int symreader_initialize(const char* path, int flags)
{
	symFlags = flags;

	bfd_init();

	abfd = bfd_openr(path, 0);
	if(!verifyLoadedSymbolInfo())
	{
		symreader_cleanup();
		return 0;
	}

	initialized = true;

	return 1;
}

int symreader_is_initialized()
{
	return initialized;
}

void* symreader_get_symbol_info(void* address, char* symName, char* filename, int* line)
{
	SymbolInfo info;
	info.pc = (bfd_vma)address;
	info.found = 0;

	if(abfd)
		bfd_map_over_sections(abfd, &findBfdAddress, &info);

	if(info.found)
	{
		if(symFlags & SYMFLAGS_DEMANGLE)
		{
			unsigned skip_first = 0;
			if(info.funcName[0] == '.' || info.funcName[0] == '$')
				++skip_first;

			char* demangle = cplus_demangle(info.funcName + skip_first,
/*				DMGL_AUTO | DMGL_PARAMS | DMGL_ANSI); */
				(1 << 8) | (1 << 0) | (1 << 1));

			if(demangle)
			{
				strncpy(symName, demangle, SYMNAME_MAX);
				free(demangle);
			}
			else
			{
				strncpy(symName, info.funcName, SYMNAME_MAX);
			}
		}
		else
		{
			strncpy(symName, info.funcName, SYMNAME_MAX);
		}

		strncpy(filename, info.filename, PATH_MAX);
		*line = info.line;
		return (void*)info.pc;
	}
	else
		return 0;
}

void symreader_cleanup()
{
	if(syms)
	{
		free(syms);
		syms = 0;
	}

	if(symTable)
	{
		free(symTable);
		symTable = 0;
	}

	if(abfd)
	{
		bfd_close(abfd);
		abfd = 0 ;
	}
}
