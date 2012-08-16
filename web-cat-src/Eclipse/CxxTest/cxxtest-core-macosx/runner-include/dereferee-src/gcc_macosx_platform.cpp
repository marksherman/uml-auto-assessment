/*
 *	This file is part of Dereferee, the diagnostic checked pointer library.
 *
 *	Dereferee is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Dereferee is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Dereferee; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

#include <cstdlib>
#include <cstdio>
#include <cstdarg>
#include <cstring>
#include <mach-o/ldsyms.h>
#include <mach-o/nlist.h>
#include <mach-o/stab.h>
#include <dereferee/platform.h>

#define CXXTEST_TRAP_SIGNALS
#define CXXTEST_TRACE_STACK
#include <cxxtest/Signals.h>

// ===========================================================================
/**
 * The gcc_macosx_platform class is an implementation of the
 * Dereferee::platform class that is intended for Mac OS X systems, where it
 * obtains symbols .stabs Mach-O load command in the current executable.
 *
 * OTHER REQUIREMENTS
 * ------------------
 * To support backtrace collection, you must set the -finstrument-functions
 * flag when compiling.
 */


// ===========================================================================

#define NO_INSTR __attribute__((no_instrument_function))

extern "C"
{
char *__cxa_demangle(const char *mangled_name, char *output_buffer,
	size_t *length, int *status);
void __cyg_profile_func_enter(void *this_fn, void *call_site) NO_INSTR;
void __cyg_profile_func_exit(void *this_fn, void *call_site) NO_INSTR;
}

void try_demangle_symbol(const char* mangled, char* demangled, size_t size);

struct backtrace_frame
{
	void *function;
	void *call_site;
};

static const size_t MAX_BACKTRACE_SIZE = 256;

static uint32_t back_trace_index = 0;
static backtrace_frame back_trace[MAX_BACKTRACE_SIZE];

struct saved_back_trace
{
	saved_back_trace* prev;
	int index;
};

static saved_back_trace* saved_back_trace_top = NULL;

struct stabInfo
{
	uint32_t address;
	const char *filename;
	int line;
	int unknown;
};

// ===========================================================================
class symbol_table
{
public:
	~symbol_table() NO_INSTR;

	static symbol_table *instance() NO_INSTR;

	const char *symbol_name_at_address(uint32_t address) NO_INSTR;
	char *demangled_name_at_address(uint32_t address) NO_INSTR;
	uint32_t source_location_at_address(uint32_t address, const char **path,
		uint32_t *line) NO_INSTR;

	void *operator new(size_t size) NO_INSTR;
	void operator delete(void* ptr) NO_INSTR;

private:
	symbol_table() NO_INSTR;

	static symbol_table *the_instance;

	uint32_t symbolCount;
	struct nlist *symbols;
	bool symbolsNeedsSort;

	uint32_t stabInfoCount;
	struct stabInfo *stabInfo;
	bool stabInfoNeedsSort;

	void process_load_commands() NO_INSTR;
	void process_symtab_command(struct symtab_command *cmd, int vmoffset) NO_INSTR;
};

symbol_table *symbol_table::the_instance = NULL;


// ===========================================================================
/**
 * Interface and implementation of the gcc_macosx_platform class.
 */

namespace DerefereeSupport
{

class gcc_macosx_platform : public Dereferee::platform
{
public:
	// -----------------------------------------------------------------------
	gcc_macosx_platform(const Dereferee::option* options);

	// -----------------------------------------------------------------------
	~gcc_macosx_platform();

	// -----------------------------------------------------------------------
	void** get_backtrace(void* instr_ptr, void* frame_ptr);

	// -----------------------------------------------------------------------
	void free_backtrace(void** backtrace);

	// -----------------------------------------------------------------------
	bool get_backtrace_frame_info(void* frame, char* function,
		char* filename, int* line_number);

	// -----------------------------------------------------------------------
	void save_current_context();

	// -----------------------------------------------------------------------
	void restore_current_context();
};

// ---------------------------------------------------------------------------
gcc_macosx_platform::gcc_macosx_platform(const Dereferee::option* options)
{
	// Force the symbol table to be created at the start of execution.
	symbol_table::instance();
}

// ---------------------------------------------------------------------------
gcc_macosx_platform::~gcc_macosx_platform()
{
}

// ------------------------------------------------------------------
void** gcc_macosx_platform::get_backtrace(void* instr_ptr, void* frame_ptr)
{
	if(back_trace_index == 0)
		return NULL;

	void** bt = (void**)calloc(back_trace_index + 1, sizeof(void*));
	int bt_index = 0;

	bt[bt_index++] = back_trace[back_trace_index - 1].function;

	for(int i = (int)back_trace_index - 1; i >= 1; i--)
	{
		bt[bt_index++] = back_trace[i].call_site;
	}

	bt[bt_index++] = NULL;
	return bt;
}

// ------------------------------------------------------------------
void gcc_macosx_platform::free_backtrace(void** backtrace)
{
	if(backtrace)
		free(backtrace);
}

// ------------------------------------------------------------------
bool gcc_macosx_platform::get_backtrace_frame_info(void* frame, char* function,
	char* filename, int* line_number)
{
	char *name = symbol_table::instance()->demangled_name_at_address(
		(uint32_t)frame);
	const char *path = "";
	uint32_t line = 0;

	if (name)
	{
		strncpy(function, name, DEREFEREE_MAX_FUNCTION_LEN - 1);

		uint32_t true_address =
			symbol_table::instance()->source_location_at_address(
			(uint32_t)frame, &path, &line);

		if (true_address)
		{
			strncpy(filename, path, DEREFEREE_MAX_FILENAME_LEN - 1);
			*line_number = line;
		}
		else
		{
			filename[0] = '\0';
			*line_number = 0;
		}

		free(name);

		return true;
	}

	return false;
}

// ---------------------------------------------------------------------------
void gcc_macosx_platform::save_current_context()
{
	saved_back_trace* new_top =
		(saved_back_trace*) calloc(1, sizeof(saved_back_trace));
	new_top->prev = saved_back_trace_top;
	new_top->index = back_trace_index;
	saved_back_trace_top = new_top;
}

// ---------------------------------------------------------------------------
void gcc_macosx_platform::restore_current_context()
{
	if (saved_back_trace_top)
	{
		saved_back_trace* new_top = saved_back_trace_top->prev;
		back_trace_index = saved_back_trace_top->index;
		free(saved_back_trace_top);
		saved_back_trace_top = new_top;
	}
}

} // end namespace DerefereeSupport

// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the platform object.
 */

Dereferee::platform* Dereferee::create_platform(
	const Dereferee::option* options)
{
	return new DerefereeSupport::gcc_macosx_platform(options);
}

void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}

// ===========================================================================

void __cyg_profile_func_enter(void *this_fn, void *call_site)
{
    if (CxxTest::__cxxtest_handlingOverflow)
		return;

    if ((int)back_trace_index == (int)MAX_BACKTRACE_SIZE)
    {
		// Assume the student has gone into infinite recursion and abort.
        CxxTest::__cxxtest_handlingOverflow = true;
        abort();
    }
	else
	{
		back_trace[back_trace_index].function = this_fn;
		back_trace[back_trace_index].call_site = call_site;
		back_trace_index++;
	}
}

void __cyg_profile_func_exit(void *this_fn, void *call_site)
{
    if (CxxTest::__cxxtest_handlingOverflow)
		return;

    if (back_trace_index)
		back_trace_index--;
}

// ===========================================================================

int symbolSorter(const void *lhs_, const void *rhs_) NO_INSTR;
int stabInfoSorter(const void *lhs_, const void *rhs_) NO_INSTR;

int symbolSorter(const void *lhs_, const void *rhs_)
{
	struct nlist *lhs = (struct nlist *)lhs_;
	struct nlist *rhs = (struct nlist *)rhs_;

	return (int)lhs->n_value - (int)rhs->n_value;
}

int stabInfoSorter(const void *lhs_, const void *rhs_)
{
	struct stabInfo *lhs = (struct stabInfo *)lhs_;
	struct stabInfo *rhs = (struct stabInfo *)rhs_;

	return (int)lhs->address - (int)rhs->address;
}

void destroy_symbol_table()
{
	delete symbol_table::instance();
}

void *symbol_table::operator new(size_t size) { return malloc(size); }

void symbol_table::operator delete(void* ptr) { free(ptr); }

symbol_table *symbol_table::instance()
{
	if(the_instance == NULL)
	{
		the_instance = new symbol_table();
		atexit(&destroy_symbol_table);
	}

	return the_instance;
}

symbol_table::symbol_table()
{
	symbolCount = 0;
	symbols = NULL;
	symbolsNeedsSort = false;

	stabInfoCount = 0;
	stabInfo = NULL;
	stabInfoNeedsSort = false;

	process_load_commands();
}

symbol_table::~symbol_table()
{
	free(symbols);
	free(stabInfo);
}

const char *symbol_table::symbol_name_at_address(uint32_t address)
{
	uint32_t low = 0;
	uint32_t high = symbolCount;

	while(low < high)
	{
		uint32_t mid = low + ((high - low) / 2);

		if(symbols[mid].n_value < address)
			low = mid + 1;
		else
			high = mid;
	}

	int index;

	if(low < symbolCount)
	{
		if(address < symbols[low].n_value)
			index = low - 1;
		else
			index = low;
	}
	else
	{
		index = symbolCount - 1;
	}

	if(index < 0)
		return NULL;
	else
		return (const char *)symbols[index].n_un.n_strx;
}

char *symbol_table::demangled_name_at_address(uint32_t address)
{
	const char *name = symbol_name_at_address(address);
	if(!name)
		return NULL;

	int status;
	char *demangled = __cxa_demangle(name + 1, NULL, NULL, &status);

	if(status != 0)
	{
		demangled = (char*)malloc(strlen(name) + 1);
		strcpy(demangled, name + ((name[0] == '_')? 1 : 0));
	}

	return demangled;
}

uint32_t symbol_table::source_location_at_address(uint32_t address,
	const char **path, uint32_t *line)
{
	uint32_t low = 0;
	uint32_t high = stabInfoCount;

	while(low < high)
	{
		uint32_t mid = low + ((high - low) / 2);

		if(stabInfo[mid].address < address)
			low = mid + 1;
		else
			high = mid;
	}

	int index;

	if(low < stabInfoCount)
	{
		if(address < stabInfo[low].address)
			index = low - 1;
		else
			index = low;
	}
	else
	{
		index = stabInfoCount - 1;
	}

	if(index < 0)
		return NULL;
	else
	{
		*path = stabInfo[index].filename;
		*line = stabInfo[index].line;
		return stabInfo[index].address;
	}
}

void symbol_table::process_load_commands()
{
	int vmoffset = (int) &_mh_execute_header;

	uint32_t num_cmds = _mh_execute_header.ncmds;

	struct load_command *load_cmd =
		(struct load_command *) (vmoffset + sizeof(struct mach_header));

	for(uint32_t i = 0; i < num_cmds; i++)
	{
		if(load_cmd->cmd == LC_SEGMENT)
		{
			struct segment_command *seg_cmd = (struct segment_command *) load_cmd;
			if (0 == strcmp(seg_cmd->segname, "__LINKEDIT"))
			{
				// .stabs data is stored in the __LINKEDIT segment, so we have to
				// get the correct virtual memory address for the beginning of this
				// data.

				vmoffset = seg_cmd->vmaddr - seg_cmd->fileoff;
			}
		}
		else if(load_cmd->cmd == LC_SYMTAB)
		{
			process_symtab_command((struct symtab_command *)load_cmd, vmoffset);
		}

		/* Advance to the next load command. */
		load_cmd = (struct load_command*)((char*)load_cmd + load_cmd->cmdsize);
	}
}

void symbol_table::process_symtab_command(struct symtab_command *cmd, int vmoffset)
{
	const char *strtab = (const char *) (vmoffset + cmd->stroff);
	struct nlist *symtab = (struct nlist *) (vmoffset + cmd->symoff);

	uint32_t nsyms = cmd->nsyms;
	uint32_t strtabsize = cmd->strsize;

	stabInfo = (struct stabInfo *)calloc(nsyms, sizeof(struct stabInfo));
	symbols = (struct nlist *)calloc(nsyms, sizeof(struct nlist));

	const char *currentFile = NULL;

	for(uint32_t i = 0; i < nsyms; i++)
	{
		if(symtab->n_type & N_STAB)
		{
			switch(symtab->n_type)
			{
				case N_SLINE:
				{
					uint32_t line = symtab->n_desc;
					uint32_t address = (uint32_t)symtab->n_value;

					// Special case for .s files?
					if (currentFile)
					{
						int len = strlen(currentFile);
						if(currentFile[len - 2] == '.' && currentFile[len - 1] == 's')
							address -= 4;

						stabInfo[stabInfoCount].filename = currentFile;
						stabInfo[stabInfoCount].address = address;
						stabInfo[stabInfoCount].line = line;

						if(stabInfoCount > 0 && stabInfoNeedsSort == false &&
							stabInfo[stabInfoCount].address <
							stabInfo[stabInfoCount - 1].address)
						{
							stabInfoNeedsSort = true;
						}

						stabInfoCount++;
					}

					break;
				}

				case N_SO:
				{
					/* Beginning of a compilation unit --
					   This directive comes in pairs, the first being the
					   absolute path to the file and the second being the
					   name of the file in that path. It also occurs singly
					   at the end of the compilation unit, where the name
					   field is "\0". */

					const char *name = strtab + symtab->n_un.n_strx;

					if(name[0] == '\0')
					{
						currentFile = NULL;
					}
					else
					{
						currentFile = name;
					}

					break;
				}

				case N_SOL:
				{
					currentFile = strtab + symtab->n_un.n_strx;
					break;
				}
			}
		}
		else
		{
			if(symtab->n_value != 0 && symtab->n_un.n_strx != 0)
			{
				if((uint32_t)symtab->n_un.n_strx < strtabsize &&
					(symtab->n_type & N_TYPE) == N_SECT)
				{
					memcpy(&symbols[symbolCount], symtab, sizeof(struct nlist));

					// Pre-add the string table offset so it can be more easily
					// accessed later.
					symbols[symbolCount].n_un.n_strx += (uint32_t)strtab;

					if(symbolCount > 0 && symbolsNeedsSort == false &&
						symbols[symbolCount].n_value <
						symbols[symbolCount - 1].n_value)
					{
						symbolsNeedsSort = true;
					}

					symbolCount++;
				}
			}
		}

		symtab++;
	}

	/* Shrink the arrays to save memory. */
	if(symbolCount > 0)
	{
		symbols = (struct nlist *)realloc(symbols,
			symbolCount * sizeof(struct nlist));
	}
	else
	{
		free(symbols);
		symbols = NULL;
	}

	if(stabInfoCount > 0)
	{
		stabInfo = (struct stabInfo *)realloc(stabInfo,
			stabInfoCount * sizeof(struct stabInfo));
	}
	else
	{
		free(stabInfo);
		stabInfo = NULL;
	}

	if(symbols != NULL && symbolsNeedsSort)
	{
		qsort(symbols, symbolCount, sizeof(struct nlist), &symbolSorter);
		symbolsNeedsSort = false;
	}

	if(stabInfo != NULL && stabInfoNeedsSort)
	{
		qsort(stabInfo, stabInfoCount, sizeof(struct stabInfo), &stabInfoSorter);
		stabInfoNeedsSort = false;
	}
}

void try_demangle_symbol(const char* mangled, char* demangled, size_t size)
{
	unsigned skip_first = 0;
	if(mangled[0] == '.' || mangled[0] == '$')
		++skip_first;

	char* ptr = __cxa_demangle(mangled + skip_first, 0, 0, 0);
	strncpy(demangled, ptr, size);
	free(ptr);
}
