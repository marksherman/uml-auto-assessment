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
#include <dereferee/platform.h>


// ===========================================================================
/**
 * The gcc_nodebug_platform class is an implementation of the
 * Dereferee::platform class that is intended for use when stack tracing in
 * Eclipse is disabled. This class does not collect any stack traces or
 * perform any symbol table operations, but it will demangle C++ type names
 * that are collected by Dereferee upon checked pointer assignment.
 *
 * OTHER REQUIREMENTS
 * ------------------
 * Since no symbol table manipulation or stack trace collection is required,
 * there are no external requirements for this platform.
 */

// ===========================================================================

extern "C"
{
char *__cxa_demangle(const char *mangled_name, char *output_buffer,
	size_t *length, int *status);
}

// ==========================================================================

namespace DerefereeSupport
{

// ===========================================================================
/**
 * Interface and implementation of the gcc_bfd_platform class.
 */
class gcc_nodebug_platform : public Dereferee::platform
{
public:
	// -----------------------------------------------------------------------
	gcc_nodebug_platform(const Dereferee::option* options);

	// -----------------------------------------------------------------------
	~gcc_nodebug_platform();

	// -----------------------------------------------------------------------
	void demangle_type_name(char* type_name);
};

// ---------------------------------------------------------------------------
gcc_nodebug_platform::gcc_nodebug_platform(const Dereferee::option* options)
{
}

// ---------------------------------------------------------------------------
gcc_nodebug_platform::~gcc_nodebug_platform()
{
}

// ---------------------------------------------------------------------------
void gcc_nodebug_platform::demangle_type_name(char* type_name)
{
	int status;
	char *demangled = __cxa_demangle(type_name, NULL, NULL, &status);

	if(status == 0)
	{
		strncpy(type_name, demangled, DEREFEREE_MAX_SYMBOL_LEN);
		free(demangled);
	}
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
	return new DerefereeSupport::gcc_nodebug_platform(options);
}

// ---------------------------------------------------------------------------
void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}
