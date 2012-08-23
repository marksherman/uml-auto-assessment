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
#include <bfd.h>
#include <dereferee/platform.h>


// ===========================================================================
/**
 * The gcc_bfd_platform class is an implementation of the Dereferee::platform
 * class that is intended for systems that support libbfd for reading the
 * symbols from an executable, and the /proc filesystem for accessing the
 * current executable reliably at runtime (to my knowledge, this includes
 * Cygwin and most BSD and Linux distributions).
 *
 * OTHER REQUIREMENTS
 * ------------------
 * To support backtrace collection, you must set the -finstrument-functions
 * flag when compiling. Symbol table access requires that you link to the
 * following libraries: bfd, iberty, intl.
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

// ==========================================================================

namespace DerefereeSupport
{

extern "C"
{
static void find_bfd_address(bfd* abfd, asection* section, void* data) NO_INSTR;
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

struct platform_symbol_info
{
	bfd_vma pc;
	const char* filename;
	const char* funcName;
	int line;
	int found;
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
	 * True if symbols were successfully loaded from the executable;
	 * otherwise, false.
	 */
	bool symbols_loaded;

	// -----------------------------------------------------------------------
	/**
	 * Initializes the symbol table.
	 */
	symbol_table() NO_INSTR;

	// -----------------------------------------------------------------------
	/**
	 * Loads the symbols from the executable and populates the internal table.
	 */
	void load_symbol_info();

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
	const char *symbol_name_at_address(bfd_vma address) NO_INSTR;

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
	char *demangled_name_at_address(bfd_vma address) NO_INSTR;

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
	bfd_vma source_location_at_address(bfd_vma address, const char **path,
		uint32_t *line) NO_INSTR;

	// -----------------------------------------------------------------------
	void *operator new(size_t size) NO_INSTR;
	void operator delete(void* ptr) NO_INSTR;
};

symbol_table *symbol_table::the_instance = NULL;

static bfd* abfd;
static asymbol** syms;
static unsigned long num_symbols;
static asymbol** sym_table;


// ===========================================================================
/**
 * Interface and implementation of the gcc_bfd_platform class.
 */
class gcc_bfd_platform : public Dereferee::platform
{
public:
	// -----------------------------------------------------------------------
	gcc_bfd_platform(const Dereferee::option* options);

	// -----------------------------------------------------------------------
	~gcc_bfd_platform();

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
gcc_bfd_platform::gcc_bfd_platform(const Dereferee::option* options)
{
	// Force the symbol table to be created when the platform is initialized,
	// if it hasn't been already.
	symbol_table::instance();
}

// ---------------------------------------------------------------------------
gcc_bfd_platform::~gcc_bfd_platform()
{
}

// ------------------------------------------------------------------
void** gcc_bfd_platform::get_backtrace(void* /* instr_ptr */,
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
void gcc_bfd_platform::free_backtrace(void** backtrace)
{
	if(backtrace)
		free(backtrace);
}

// ------------------------------------------------------------------
bool gcc_bfd_platform::get_backtrace_frame_info(void* frame, char* function,
	char* filename, int* line_number)
{
	char *name = symbol_table::instance()->demangled_name_at_address(
		(bfd_vma)frame);
	const char *path = "";
	uint32_t line = 0;

	if (name)
	{
		strncpy(function, name, DEREFEREE_MAX_FUNCTION_LEN - 1);

		bfd_vma true_address =
			symbol_table::instance()->source_location_at_address(
			(bfd_vma)frame, &path, &line);

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
void gcc_bfd_platform::demangle_type_name(char* type_name)
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
void gcc_bfd_platform::save_current_context()
{
	saved_back_trace* new_top =
		(saved_back_trace*) calloc(1, sizeof(saved_back_trace));
	new_top->prev = saved_back_trace_top;
	new_top->index = back_trace_index;
	saved_back_trace_top = new_top;
}

// ---------------------------------------------------------------------------
void gcc_bfd_platform::restore_current_context()
{
	if (saved_back_trace_top)
	{
		saved_back_trace* new_top = saved_back_trace_top->prev;
		back_trace_index = saved_back_trace_top->index;
		free(saved_back_trace_top);
		saved_back_trace_top = new_top;
	}
}


// ===========================================================================

static void find_bfd_address(bfd* abfd, asection* section, void* data)
{
	platform_symbol_info* info = (platform_symbol_info*)data;
	if(info->found)
		return;

	if(!(bfd_get_section_flags(abfd, section) & SEC_ALLOC))
		return;

	bfd_vma pc = info->pc;
	if(pc < section->vma)
		return;

	pc -= section->vma;
	if(pc >= section->size)
		return;

	info->found = bfd_find_nearest_line(abfd, section, syms, pc,
		&info->filename, &info->funcName, (unsigned int*)(&info->line));
}

// ---------------------------------------------------------------------------
void destroy_symbol_table()
{
	delete symbol_table::instance();
}

// ---------------------------------------------------------------------------
void *symbol_table::operator new(size_t size) { return malloc(size); }

// ---------------------------------------------------------------------------
void symbol_table::operator delete(void* ptr) { free(ptr); }

// ---------------------------------------------------------------------------
symbol_table *symbol_table::instance()
{
	if(the_instance == NULL)
	{
		the_instance = new symbol_table();
		atexit(&destroy_symbol_table);
	}

	return the_instance;
}

// ---------------------------------------------------------------------------
symbol_table::symbol_table()
{
	symbols_loaded = false;

	bfd_init();

	pid_t pid = getpid();
	char proc_path[512];
	snprintf(proc_path, sizeof(proc_path), "/proc/%lu/exe", (unsigned long)pid);
	abfd = bfd_openr(proc_path, 0);

	load_symbol_info();
}

// ---------------------------------------------------------------------------
symbol_table::~symbol_table()
{
	if(syms)
		free(syms);

	if(sym_table)
		free(sym_table);

//	if(abfd)
//		bfd_close(abfd);
}

// ---------------------------------------------------------------------------
void symbol_table::load_symbol_info()
{
	char** matching;

	if(!abfd)
		return;

	if(bfd_check_format(abfd, bfd_archive))
		return;

	if(!bfd_check_format_matches(abfd, bfd_object, &matching))
	{
		free(matching);
		return;
	}

	if(!(bfd_get_file_flags(abfd) & HAS_SYMS))
		return;

	unsigned int size;
	num_symbols = bfd_read_minisymbols(abfd, 0, (void**)&syms, &size);

	if(!num_symbols)
		num_symbols = bfd_read_minisymbols(abfd, 1, (void**)&syms, &size);

	// supporting dynamic symbols
	long storage = bfd_get_symtab_upper_bound(abfd);
	if(storage < 1)
		return;

	syms = (asymbol**)malloc(storage);
	num_symbols = bfd_canonicalize_symtab(abfd, syms);
	if(num_symbols < 1)
	{
		free(syms);
		return;
	}

	symbols_loaded = true;
}

// ---------------------------------------------------------------------------
const char *symbol_table::symbol_name_at_address(bfd_vma address)
{
	if(!symbols_loaded)
		return NULL;

	platform_symbol_info info;
	info.pc = address;
	info.found = 0;

	if(abfd)
		bfd_map_over_sections(abfd, &find_bfd_address, &info);

	if(info.found)
		return info.funcName;
	else
		return NULL;
}

// ---------------------------------------------------------------------------
char *symbol_table::demangled_name_at_address(bfd_vma address)
{
	const char *name = symbol_name_at_address(address);
	if(!name)
		return NULL;

	int status;
	char *demangled = __cxa_demangle(name, NULL, NULL, &status);

	if(status != 0)
	{
		demangled = (char*)malloc(strlen(name) + 1);
		strcpy(demangled, name + ((name[0] == '_')? 1 : 0));
	}

	return demangled;
}

// ---------------------------------------------------------------------------
bfd_vma symbol_table::source_location_at_address(bfd_vma address,
	const char **path, uint32_t *line)
{
	if(!symbols_loaded)
		return 0;

	platform_symbol_info info;
	info.pc = address;
	info.found = 0;

	if(abfd)
		bfd_map_over_sections(abfd, &find_bfd_address, &info);

	if(info.found)
	{
		*path = info.filename;
		*line = info.line;
		return (bfd_vma)info.pc;
	}
	else
		return 0;
}

// ---------------------------------------------------------------------------
void try_demangle_symbol(const char* mangled, char* demangled, size_t size)
{
	unsigned skip_first = 0;
	if(mangled[0] == '.' || mangled[0] == '$')
		++skip_first;

	char* ptr = __cxa_demangle(mangled + skip_first, 0, 0, 0);
	strncpy(demangled, ptr, size);
	free(ptr);
}

} // end namespace DerefereeSupport

// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the listener object.
 */

// ---------------------------------------------------------------------------
Dereferee::platform* Dereferee::create_platform(
		const Dereferee::option* options)
{
	return new DerefereeSupport::gcc_bfd_platform(options);
}

// ---------------------------------------------------------------------------
void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}

// ===========================================================================

namespace CxxTest {
	extern bool __cxxtest_handlingOverflow;
}

void __cyg_profile_func_enter(void *this_fn, void *call_site)
{
	using namespace DerefereeSupport;

	// If the user makes function calls too deep and overflows the frame
	// tracking buffer, we currently just stop tracking. In a future version,
	// we may wish to change this to drop the *earliest* frames, rather than
	// the latest ones.

    if ((int)back_trace_index != (int)MAX_BACKTRACE_SIZE)
	{
		CxxTest::__cxxtest_handlingOverflow = false;
		back_trace[back_trace_index].function = this_fn;
		back_trace[back_trace_index].call_site = call_site;
		back_trace_index++;
	}
	else if (!CxxTest::__cxxtest_handlingOverflow)
	{
		CxxTest::__cxxtest_handlingOverflow = true;
		int* p = 0;
		*p = 0xDEADBEEF;
	}
}

// ---------------------------------------------------------------------------
void __cyg_profile_func_exit(void *this_fn, void *call_site)
{
	using namespace DerefereeSupport;

	if (CxxTest::__cxxtest_handlingOverflow)
		return;

    if (back_trace_index)
    	back_trace_index--;
}
