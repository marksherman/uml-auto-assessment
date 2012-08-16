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

// ===========================================================================
/**
 * The gcc_macosx_platform class is an implementation of the
 * Dereferee::platform class that is intended for Mac OS X systems, where it
 * obtains symbols from the .stabs Mach-O load command in the running
 * executable.
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
static const size_t MAX_SAVED_CONTEXTS = 64;

static uint32_t back_trace_index = 0;
static backtrace_frame back_trace[MAX_BACKTRACE_SIZE];

static uint32_t saved_back_trace_top = 0;
static uint32_t saved_back_trace_indices[MAX_SAVED_CONTEXTS];

struct stab_info
{
	uintptr_t address;
	const char *filename;
	int line;
	int unknown;
};

// ===========================================================================
/**
 * The symbol_table is a singleton that loads and maintains the symbol table
 * of the executable when the platform is initialized.
 */
class symbol_table
{
private:
	/**
	 * The single instance of the symbol_table class.
	 */
	static symbol_table *the_instance;

	/**
	 * True if the executable is x86_64; false for i386 or ppc.
	 */
	bool is_64_bit;

	/**
	 * The number of symbols loaded.
	 */
	uint32_t symbol_count;
	
	/**
	 * The array of nlist(_64) structures that contains the symbols.
	 */
	void *symbols;
	
	/**
	 * True if the symbols need to be sorted after loading, otherwise false.
	 */
	bool symbols_need_sort;

	/**
	 * The number of stab_info objects loaded.
	 */
	uint32_t stab_info_count;

	/**
	 * The array of stab_info structures that contains the symbols.
	 */
	struct stab_info *stab_info;

	/**
	 * True if the stab_infos need to be sorted after loading, otherwise false.
	 */
	bool stab_info_needs_sort;
	
	/**
	 * A pointer to the executable's string table that contains the filenames
	 * and symbol names used in the program.
	 */
	const char *string_table;

	// -----------------------------------------------------------------------
	/**
	 * Initializes the symbol table.
	 */
	symbol_table() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Checks the 64-bitness of the executable and calls the appropriate
	 * processor function.
	 */
	void process_executable() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Iterates over the Mach-O load commands in a 32-bit executable.
	 */
	void process_load_commands() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Iterates over the Mach-O load commands in a 64-bit executable.
	 */
	void process_load_commands_64() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Extracts the symbols from the LC_SYMTAB load command of a 32-bit
	 * executable.
	 */
	void process_symtab_command(struct symtab_command *cmd, intptr_t vmoffset)
		NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Extracts the symbols from the LC_SYMTAB load command of a 64-bit
	 * executable.
	 */
	void process_symtab_command_64(struct symtab_command *cmd, intptr_t vmoffset)
		NO_INSTR;

public:
	// -----------------------------------------------------------------------
	/**
	 * Destroys the symbol table instance. This occurs as the result of an
	 * atexit() handler that is installed when the symbol table is created.
	 */
	~symbol_table() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Gets the single instance of the symbol_table class.
	 *
	 * @returns the single instance of the symbol_table class
	 */
	static symbol_table *instance() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Gets the raw (mangled) name of the symbol at the specified address.
	 *
	 * @param address the address of the symbol
	 *
	 * @returns the mangled name of the symbol if found, or NULL if there was
	 *     no symbol at that address
	 */
	const char *symbol_name_at_address(uintptr_t address) NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Gets the human-readable (demangled) name of the symbol at the specified
	 * address.
	 *
	 * @param address the address of the symbol
	 *
	 * @returns the demangled name of the symbol if found, or NULL if there
	 *     was no symbol at that address
	 */
	char *demangled_name_at_address(uintptr_t address) NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Gets the source file path and line number of the symbol at the
	 * specified address.
	 *
	 * @param address the address of the symbol
	 * @param path a pointer to a "const char *" that will store the address
	 *     of the string that contains the source file path
	 * @param line a pointer to a uint32_t that will store the line number
	 *
	 * @returns the actual address of the symbol (since the one passed in
	 *     may be offset) if it was found, or NULL if there was no symbol at
	 *     that address
	 */
	uintptr_t source_location_at_address(uintptr_t address, const char **path,
		uint32_t *line) NO_INSTR;

	// -----------------------------------------------------------------------
	void *operator new(size_t size) NO_INSTR;
	void operator delete(void* ptr) NO_INSTR;
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
	void demangle_type_name(char* type_name);

	// -----------------------------------------------------------------------
	void save_current_context();

	// -----------------------------------------------------------------------
	void restore_current_context();
};

// ---------------------------------------------------------------------------
gcc_macosx_platform::gcc_macosx_platform(
		const Dereferee::option* /* options */)
{
	// Force the symbol table to be created at the start of execution.
	symbol_table::instance();
}

// ---------------------------------------------------------------------------
gcc_macosx_platform::~gcc_macosx_platform()
{
}

// ------------------------------------------------------------------
void** gcc_macosx_platform::get_backtrace(void* /* instr_ptr */,
		void* /* frame_ptr */)
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
	filename[0] = '\0';
	*line_number = 0;

	char *name = symbol_table::instance()->demangled_name_at_address(
		(uintptr_t)frame);

	const char *path = "";
	uint32_t line = 0;

	if (name)
	{
		strncpy(function, name, DEREFEREE_MAX_FUNCTION_LEN - 1);

		uintptr_t true_address =
			symbol_table::instance()->source_location_at_address(
			(uintptr_t)frame, &path, &line);

		if (true_address)
		{
			strncpy(filename, path, DEREFEREE_MAX_FILENAME_LEN - 1);
			*line_number = line;
		}

		free(name);

		return true;
	}

	return false;
}

// ---------------------------------------------------------------------------
void gcc_macosx_platform::demangle_type_name(char* type_name)
{
	int status;
	char *demangled = __cxa_demangle(type_name, NULL, NULL, &status);

	if(status == 0)
	{
		strncpy(type_name, demangled, DEREFEREE_MAX_SYMBOL_LEN);
		free(demangled);
	}
}

// ---------------------------------------------------------------------------
void gcc_macosx_platform::save_current_context()
{
	if (saved_back_trace_top > MAX_SAVED_CONTEXTS)
	{
		exit(1);
	}
	else
	{
		saved_back_trace_indices[saved_back_trace_top] = back_trace_index;
		saved_back_trace_top++;
	}
}

// ---------------------------------------------------------------------------
void gcc_macosx_platform::restore_current_context()
{
	if (saved_back_trace_top)
	{
		saved_back_trace_top--;
		back_trace_index = saved_back_trace_indices[saved_back_trace_top];
	}
}

} // end namespace DerefereeSupport

// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the platform object.
 */

	// -----------------------------------------------------------------------
Dereferee::platform* Dereferee::create_platform(
	const Dereferee::option* options)
{
	return new DerefereeSupport::gcc_macosx_platform(options);
}

// -----------------------------------------------------------------------
void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}

// ===========================================================================

void __cyg_profile_func_enter(void *this_fn, void *call_site)
{
    if ((int)back_trace_index != (int)MAX_BACKTRACE_SIZE)
	{
		back_trace[back_trace_index].function = this_fn;
		back_trace[back_trace_index].call_site = call_site;
		back_trace_index++;
	}
}

// -----------------------------------------------------------------------
void __cyg_profile_func_exit(void * /* this_fn */, void * /* call_site */)
{
    if (back_trace_index)
		back_trace_index--;
}

// ===========================================================================

int symbol_sorter(const void *lhs_, const void *rhs_) NO_INSTR;
int stab_info_sorter(const void *lhs_, const void *rhs_) NO_INSTR;

// -----------------------------------------------------------------------
int symbol_sorter(const void *lhs_, const void *rhs_)
{
	struct nlist *lhs = (struct nlist *)lhs_;
	struct nlist *rhs = (struct nlist *)rhs_;

	return (int)lhs->n_value - (int)rhs->n_value;
}

// -----------------------------------------------------------------------
int stab_info_sorter(const void *lhs_, const void *rhs_)
{
	struct stab_info *lhs = (struct stab_info *)lhs_;
	struct stab_info *rhs = (struct stab_info *)rhs_;

	return (int)lhs->address - (int)rhs->address;
}

// -----------------------------------------------------------------------
void destroy_symbol_table()
{
	delete symbol_table::instance();
}

// -----------------------------------------------------------------------
void *symbol_table::operator new(size_t size) { return malloc(size); }

// -----------------------------------------------------------------------
void symbol_table::operator delete(void* ptr) { free(ptr); }

// -----------------------------------------------------------------------
symbol_table *symbol_table::instance()
{
	if(the_instance == NULL)
	{
		the_instance = new symbol_table();
		atexit(&destroy_symbol_table);
	}

	return the_instance;
}

// -----------------------------------------------------------------------
symbol_table::symbol_table()
{
	symbol_count = 0;
	symbols = NULL;
	symbols_need_sort = false;

	stab_info_count = 0;
	stab_info = NULL;
	stab_info_needs_sort = false;

	process_executable();
}

// -----------------------------------------------------------------------
symbol_table::~symbol_table()
{
	free(symbols);
	free(stab_info);
}

// -----------------------------------------------------------------------
const char *symbol_table::symbol_name_at_address(uintptr_t address)
{
	uint32_t low = 0;
	uint32_t high = symbol_count;

	if (is_64_bit)
	{
		struct nlist_64 *syms = (struct nlist_64 *) symbols;

		while(low < high)
		{
			uint32_t mid = low + ((high - low) / 2);

			if(syms[mid].n_value < address)
				low = mid + 1;
			else
				high = mid;
		}

		int index;

		if(low < symbol_count)
		{
			if(address < syms[low].n_value)
				index = low - 1;
			else
				index = low;
		}
		else
		{
			index = symbol_count - 1;
		}

		if(index < 0)
			return NULL;
		else
			return string_table + syms[index].n_un.n_strx;
	}
	else
	{
		struct nlist *syms = (struct nlist *) symbols;

		while(low < high)
		{
			uint32_t mid = low + ((high - low) / 2);

			if(syms[mid].n_value < address)
				low = mid + 1;
			else
				high = mid;
		}

		int index;

		if(low < symbol_count)
		{
			if(address < syms[low].n_value)
				index = low - 1;
			else
				index = low;
		}
		else
		{
			index = symbol_count - 1;
		}

		if(index < 0)
			return NULL;
		else
			return string_table + syms[index].n_un.n_strx;
	}
}

// -----------------------------------------------------------------------
char *symbol_table::demangled_name_at_address(uintptr_t address)
{
	const char *name = symbol_name_at_address(address);

	if (!name)
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

// -----------------------------------------------------------------------
uintptr_t symbol_table::source_location_at_address(uintptr_t address,
	const char **path, uint32_t *line)
{
	uint32_t low = 0;
	uint32_t high = stab_info_count;

	while(low < high)
	{
		uint32_t mid = low + ((high - low) / 2);

		if(stab_info[mid].address < address)
			low = mid + 1;
		else
			high = mid;
	}

	int index;

	if(low < stab_info_count)
	{
		if(address < stab_info[low].address)
			index = low - 1;
		else
			index = low;
	}
	else
	{
		index = stab_info_count - 1;
	}

	if(index < 0)
		return NULL;
	else
	{
		*path = stab_info[index].filename;
		*line = stab_info[index].line;
		return stab_info[index].address;
	}
}

// -----------------------------------------------------------------------
void symbol_table::process_executable()
{
	is_64_bit = false;

	if (_mh_execute_header.magic == MH_MAGIC)
	{
		process_load_commands();
	}
	else if (_mh_execute_header.magic == MH_MAGIC_64)
	{
		is_64_bit = true;
		process_load_commands_64();
	}
}

// -----------------------------------------------------------------------
void symbol_table::process_load_commands()
{
	intptr_t vmoffset = (intptr_t) &_mh_execute_header;

	uint32_t num_cmds = _mh_execute_header.ncmds;

	struct load_command *load_cmd =
		(struct load_command *) (vmoffset + sizeof(struct mach_header));

	for(uint32_t i = 0; i < num_cmds; i++)
	{
		if(load_cmd->cmd == LC_SEGMENT)
		{
			struct segment_command *seg_cmd =
				(struct segment_command *) load_cmd;

			if (0 == strcmp(seg_cmd->segname, "__LINKEDIT"))
			{
				// .stabs data is stored in the __LINKEDIT segment, so we have
				// to get the correct virtual memory address for the beginning
				// of this data.

				vmoffset = seg_cmd->vmaddr - seg_cmd->fileoff;
			}
		}
		else if(load_cmd->cmd == LC_SYMTAB)
		{
			process_symtab_command(
				(struct symtab_command *)load_cmd, vmoffset);
		}

		/* Advance to the next load command. */
		load_cmd =
			(struct load_command*)((char*)load_cmd + load_cmd->cmdsize);
	}
}

// -----------------------------------------------------------------------
void symbol_table::process_symtab_command(struct symtab_command *cmd,
	intptr_t vmoffset)
{
	string_table = (const char *) (vmoffset + cmd->stroff);
	struct nlist *symtab = (struct nlist *) (vmoffset + cmd->symoff);

	uint32_t nsyms = cmd->nsyms;
	uint32_t strtabsize = cmd->strsize;

	stab_info = (struct stab_info *)calloc(nsyms, sizeof(struct stab_info));
	struct nlist *_symbols = (struct nlist *)calloc(nsyms,
			sizeof(struct nlist));
	symbols = _symbols;

	const char *current_file = NULL;

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
					if (current_file)
					{
						int len = strlen(current_file);
						if(current_file[len - 2] == '.' &&
							current_file[len - 1] == 's')
						{
							address -= 4;
						}

						stab_info[stab_info_count].filename = current_file;
						stab_info[stab_info_count].address = address;
						stab_info[stab_info_count].line = line;

						if(stab_info_count > 0 && stab_info_needs_sort == false &&
							stab_info[stab_info_count].address <
							stab_info[stab_info_count - 1].address)
						{
							stab_info_needs_sort = true;
						}

						stab_info_count++;
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

					const char *name = string_table + symtab->n_un.n_strx;

					if(name[0] == '\0')
					{
						current_file = NULL;
					}
					else
					{
						current_file = name;
					}

					break;
				}

				case N_SOL:
				{
					current_file = string_table + symtab->n_un.n_strx;
					break;
				}
			}
		}
		else
		{
			if(symtab->n_value != 0 && symtab->n_un.n_strx != 0)
			{
				if((uintptr_t)symtab->n_un.n_strx < strtabsize &&
					(symtab->n_type & N_TYPE) == N_SECT)
				{
					memcpy(&_symbols[symbol_count], symtab,
						sizeof(struct nlist));

					if(symbol_count > 0 && symbols_need_sort == false &&
							_symbols[symbol_count].n_value <
							_symbols[symbol_count - 1].n_value)
					{
						symbols_need_sort = true;
					}

					symbol_count++;
				}
			}
		}

		symtab++;
	}

	/* Shrink the arrays to save memory. */
	if(symbol_count > 0)
	{
		_symbols = (struct nlist *)realloc(_symbols,
			symbol_count * sizeof(struct nlist));
		symbols = _symbols;
	}
	else
	{
		free(symbols);
		symbols = NULL;
	}

	if(stab_info_count > 0)
	{
		stab_info = (struct stab_info *)realloc(stab_info,
			stab_info_count * sizeof(struct stab_info));
	}
	else
	{
		free(stab_info);
		stab_info = NULL;
	}

	if(symbols != NULL && symbols_need_sort)
	{
		qsort(symbols, symbol_count, sizeof(struct nlist), &symbol_sorter);
		symbols_need_sort = false;
	}

	if(stab_info != NULL && stab_info_needs_sort)
	{
		qsort(stab_info, stab_info_count, sizeof(struct stab_info),
			&stab_info_sorter);
		stab_info_needs_sort = false;
	}
}

// -----------------------------------------------------------------------
void symbol_table::process_load_commands_64()
{
	intptr_t vmoffset = (intptr_t) &_mh_execute_header;

	uint32_t num_cmds = _mh_execute_header.ncmds;

	struct load_command *load_cmd =
		(struct load_command *) (vmoffset + sizeof(struct mach_header_64));

	for(uint32_t i = 0; i < num_cmds; i++)
	{
		if(load_cmd->cmd == LC_SEGMENT_64)
		{
			struct segment_command_64 *seg_cmd =
				(struct segment_command_64 *) load_cmd;

			if (0 == strcmp(seg_cmd->segname, "__LINKEDIT"))
			{
				// .stabs data is stored in the __LINKEDIT segment, so we have
				// to get the correct virtual memory address for the beginning
				// of this data.

				vmoffset = seg_cmd->vmaddr - seg_cmd->fileoff;
			}
		}
		else if(load_cmd->cmd == LC_SYMTAB)
		{
			process_symtab_command_64(
				(struct symtab_command *)load_cmd, vmoffset);
		}

		/* Advance to the next load command. */
		load_cmd =
			(struct load_command*)((char*)load_cmd + load_cmd->cmdsize);
	}
}

// -----------------------------------------------------------------------
void symbol_table::process_symtab_command_64(struct symtab_command *cmd,
	intptr_t vmoffset)
{
	string_table = (const char *) (vmoffset + cmd->stroff);
	struct nlist_64 *symtab = (struct nlist_64 *) (vmoffset + cmd->symoff);

	uint32_t nsyms = cmd->nsyms;
	uint32_t strtabsize = cmd->strsize;

	stab_info = (struct stab_info *)calloc(nsyms, sizeof(struct stab_info));
	struct nlist_64 *_symbols = (struct nlist_64 *)calloc(nsyms,
			sizeof(struct nlist_64));
	symbols = _symbols;

	const char *current_file = NULL;

	for(uint32_t i = 0; i < nsyms; i++)
	{
		if(symtab->n_type & N_STAB)
		{
			switch(symtab->n_type)
			{
				case N_SLINE:
				{
					uint32_t line = symtab->n_desc;
					uintptr_t address = (uintptr_t)symtab->n_value;

					// Special case for .s files?
					if (current_file)
					{
						int len = strlen(current_file);
						if(current_file[len - 2] == '.' &&
							current_file[len - 1] == 's')
						{
							address -= 4;
						}

						stab_info[stab_info_count].filename = current_file;
						stab_info[stab_info_count].address = address;
						stab_info[stab_info_count].line = line;
						
						if(stab_info_count > 0 && stab_info_needs_sort == false &&
							stab_info[stab_info_count].address <
							stab_info[stab_info_count - 1].address)
						{
							stab_info_needs_sort = true;
						}

						stab_info_count++;
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

					const char *name = string_table + symtab->n_un.n_strx;

					if(name[0] == '\0')
					{
						current_file = NULL;
					}
					else
					{
						current_file = name;
					}

					break;
				}

				case N_SOL:
				{
					current_file = string_table + symtab->n_un.n_strx;
					break;
				}
			}
		}
		else
		{
			if(symtab->n_value != 0 && symtab->n_un.n_strx != 0)
			{
				if((uintptr_t)symtab->n_un.n_strx < strtabsize &&
					(symtab->n_type & N_TYPE) == N_SECT)
				{
					memcpy(&_symbols[symbol_count], symtab,
						sizeof(struct nlist_64));

					if(symbol_count > 0 && symbols_need_sort == false &&
							_symbols[symbol_count].n_value <
							_symbols[symbol_count - 1].n_value)
					{
						symbols_need_sort = true;
					}

					symbol_count++;
				}
			}
		}

		symtab++;
	}

	/* Shrink the arrays to save memory. */
	if(symbol_count > 0)
	{
		_symbols = (struct nlist_64 *)realloc(symbols,
			symbol_count * sizeof(struct nlist_64));
		symbols = _symbols;
	}
	else
	{
		free(symbols);
		symbols = NULL;
	}

	if(stab_info_count > 0)
	{
		stab_info = (struct stab_info *)realloc(stab_info,
			stab_info_count * sizeof(struct stab_info));
	}
	else
	{
		free(stab_info);
		stab_info = NULL;
	}

	if(symbols != NULL && symbols_need_sort)
	{
		qsort(symbols, symbol_count, sizeof(struct nlist_64), &symbol_sorter);
		symbols_need_sort = false;
	}

	if(stab_info != NULL && stab_info_needs_sort)
	{
		qsort(stab_info, stab_info_count, sizeof(struct stab_info),
			&stab_info_sorter);
		stab_info_needs_sort = false;
	}
}

// -----------------------------------------------------------------------
void try_demangle_symbol(const char* mangled, char* demangled, size_t size)
{
	unsigned skip_first = 0;
	if(mangled[0] == '.' || mangled[0] == '$')
		++skip_first;

	char* ptr = __cxa_demangle(mangled + skip_first, 0, 0, 0);
	strncpy(demangled, ptr, size);
	free(ptr);
}
