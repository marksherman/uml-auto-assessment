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
 * The empty_platform class is an implementation of the Dereferee::platform
 * class that acts as an empty placeholder when platform-specific
 * functionality is not needed or an appropriate platform module is
 * unavailable.
 */

// ===========================================================================

namespace DerefereeSupport
{
	
// ===========================================================================
/**
 * Interface and implementation of the empty_platform class.
 */
class empty_platform : public Dereferee::platform
{
public:
	// -----------------------------------------------------------------------
	empty_platform(const Dereferee::option* /* options */ ) { }

	// -----------------------------------------------------------------------
	~empty_platform() { }

	// -----------------------------------------------------------------------
	void** get_backtrace(void* /* instr_ptr */, void* /* frame_ptr */ )
		{ return NULL; }

	// -----------------------------------------------------------------------
	void free_backtrace(void** /* backtrace */ ) { }

	// -----------------------------------------------------------------------
	bool get_backtrace_frame_info(void* /* frame */, char* /* function */,
		char* /* filename */, int* /* line_number */ ) { return false; }
};

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
	return new DerefereeSupport::empty_platform(options);
}

// ---------------------------------------------------------------------------
void Dereferee::destroy_platform(Dereferee::platform* platform)
{
	delete platform;
}
