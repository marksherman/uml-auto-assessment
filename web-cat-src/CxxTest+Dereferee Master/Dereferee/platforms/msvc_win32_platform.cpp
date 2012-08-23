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

#ifndef _CRT_SECURE_NO_DEPRECATE
#define _CRT_SECURE_NO_DEPRECATE
#endif

#undef _WIN32_WINNT
#define _WIN32_WINNT 0x0400

#include <cstdlib>
#include <cstdio>
#include <cstdarg>
#include <cstring>
#include <windows.h>
#include <dbghelp.h>
#include <dereferee/platform.h>

// ===========================================================================
/**
 * The msvc_win32_platform class is an implementation of the
 * Dereferee::platform class that supports executables compiled with
 * Microsoft Visual C++ 2005 or higher on Windows.
 *
 * OTHER REQUIREMENTS
 * ------------------
 * To support symbol table access, the executable should be compiled with the
 * /Zi option to generate complete debugging information. It should also be
 * linked with dbghelp.lib; pragmas have been specified below to eliminate the
 * need to alter your project settings.
 */
#pragma comment(lib, "dbghelp")


// ===========================================================================

/**
 * _ReturnAddress is a Visual C++ compiler intrinsic, similar to gcc's
 * __builtin_return_address, that returns the address that the current
 * function will return to when it exits. What we do here is wrap a call to
 * this inside its own function, which has the effect of giving us the current
 * instruction pointer at the location where we call it.
 */
extern "C"
{
void* _ReturnAddress(void);
}
#pragma intrinsic(_ReturnAddress)

#pragma auto_inline(off)
DWORD_PTR get_current_eip()
{
	return (DWORD_PTR)_ReturnAddress();
}
#pragma auto_inline(on)


// ===========================================================================

namespace DerefereeSupport
{

/**
 * Interface and implementation of the msvc_win32_platform class.
 */
class win32_platform : public Dereferee::platform
{
public:
	// -----------------------------------------------------------------------
	win32_platform(const Dereferee::option* options);
	
	// -----------------------------------------------------------------------
	~win32_platform();

	// -----------------------------------------------------------------------
	void** get_backtrace(void* instr_ptr, void* frame_ptr);
	
	// -----------------------------------------------------------------------
	void free_backtrace(void** backtrace);

	// -----------------------------------------------------------------------
	bool get_backtrace_frame_info(void* frame, char* function,
		char* filename, int* line_number);
};

// ---------------------------------------------------------------------------
win32_platform::win32_platform(const Dereferee::option* /*options*/ )
{
	// Initialize the symbol engine.
	DWORD sym_options = SymGetOptions();
	sym_options |= SYMOPT_LOAD_LINES;
	SymSetOptions(sym_options);
	SymInitialize(GetCurrentProcess(), NULL, TRUE);
}

// ---------------------------------------------------------------------------
win32_platform::~win32_platform()
{
	SymCleanup(GetCurrentProcess());
}

// ------------------------------------------------------------------
void** win32_platform::get_backtrace(void* instr_ptr, void* frame_ptr)
{
	DWORD_PTR ebp_ptr;
	
	if(frame_ptr)
	{
		ebp_ptr = (DWORD_PTR)frame_ptr;
	}
	else
	{
		__asm { mov [ebp_ptr], ebp }
	}

	DWORD_PTR eip;
	
	if(instr_ptr)
	{
		eip = (DWORD_PTR)instr_ptr;
	}
	else
	{
		eip = get_current_eip();
	}

	CONTEXT ctx = { 0 };

	STACKFRAME64 frame = { 0 };
	frame.AddrPC.Offset = eip;
	frame.AddrPC.Mode = AddrModeFlat;
	frame.AddrFrame.Offset = ebp_ptr;
	frame.AddrFrame.Mode = AddrModeFlat;

	size_t bt_cap = 4;
	size_t bt_size = 0;
	void** bt = (void**)malloc((bt_cap + 1) * sizeof(void*));

	bool done = false;

	while(!done)
	{
		if(!StackWalk64(IMAGE_FILE_MACHINE_I386, GetCurrentProcess(),
			GetCurrentThread(), &frame, &ctx, NULL, SymFunctionTableAccess64,
			SymGetModuleBase64, NULL))
		{
			done = true;
		}
		else if(frame.AddrFrame.Offset == 0)
		{
			done = true;
		}
		else
		{		
			// Grow the backtrace buffer if we need more room.
			if(bt_cap == bt_size)
			{
				bt_cap *= 2;
				bt = (void**)realloc(bt, (bt_cap + 1) * sizeof(void*));
			}

			bt[bt_size++] = (void*)frame.AddrPC.Offset;
		}
	}

	bt[bt_size++] = NULL;

	bt = (void**)realloc(bt, bt_size * sizeof(void*));
	return bt;
}

// ------------------------------------------------------------------
void win32_platform::free_backtrace(void** backtrace)
{
	if(backtrace)
		free(backtrace);
}

// ------------------------------------------------------------------
bool win32_platform::get_backtrace_frame_info(void* frame, char* function,
	char* filename, int* line_number)
{
	IMAGEHLP_SYMBOL64* symbol = (IMAGEHLP_SYMBOL64*)malloc(
		sizeof(IMAGEHLP_SYMBOL64) + DEREFEREE_MAX_FUNCTION_LEN);

	symbol->SizeOfStruct = sizeof(IMAGEHLP_SYMBOL64);
	symbol->MaxNameLength = DEREFEREE_MAX_FUNCTION_LEN;
	memset((char*)symbol + sizeof(IMAGEHLP_SYMBOL64), 0,
		DEREFEREE_MAX_FUNCTION_LEN);

	DWORD64 displacement = 0;
	BOOL res = SymGetSymFromAddr64(GetCurrentProcess(), (DWORD64)frame,
		&displacement, symbol);

	if(res)
	{
		strncpy(function, (char*)symbol->Name,
			DEREFEREE_MAX_FUNCTION_LEN - 1);

		IMAGEHLP_LINE64 line_info = { 0 };
		line_info.SizeOfStruct = sizeof(line_info);
		DWORD displacement2 = 0;
		
		res = SymGetLineFromAddr64(GetCurrentProcess(), (DWORD64)frame,
			&displacement2, &line_info);

		if(res)
		{
			strncpy(filename, (char*)line_info.FileName,
				DEREFEREE_MAX_FILENAME_LEN - 1);
			*line_number = (int)line_info.LineNumber;
		}
		else
		{
			filename[0] = '\0';
			*line_number = 0;
		}

		free(symbol);
		return true;
	}
	else
	{
		free(symbol);
		return false;
	}
}

} // end namespace DerefereeSupport

// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the platform object.
 */

// ---------------------------------------------------------------------------
Dereferee::platform* Dereferee::create_platform(
	const Dereferee::option* options)
{
	return new DerefereeSupport::win32_platform(options);
}

// ---------------------------------------------------------------------------
void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}
