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

#ifndef DEREFEREE_ALLOCATION_INFO_IMPL_H
#define DEREFEREE_ALLOCATION_INFO_IMPL_H

#include <cstdarg>
#include <dereferee/listener.h>
#include <dereferee/manager.h>

namespace Dereferee
{

// ===========================================================================
/**
 * A concrete implementation of the Dereferee::allocation_info interface.
 * This class is not intended to be used by clients.
 */
class allocation_info_impl : public allocation_info
{
private:
	/**
	 * The internal mem_info structure that contains the information that
	 * will be exposed by this class.
	 */
	mem_info* info;

public:
	// -----------------------------------------------------------------------
	/**
	 * Initializes a new allocation_info_impl object with the specified
	 * mem_info.
	 *
	 * @param i the mem_info object containing the information about the
	 *     memory block
	 */
	allocation_info_impl(mem_info& i);
	
	// -----------------------------------------------------------------------
	const void* address() const;

	// -----------------------------------------------------------------------
	size_t block_size() const;

	// -----------------------------------------------------------------------
	bool is_array() const;

	// -----------------------------------------------------------------------
	size_t array_size() const;

	// -----------------------------------------------------------------------
	const char* type_name() const;

	// -----------------------------------------------------------------------
	void** backtrace() const;
	
	// -----------------------------------------------------------------------
    void* user_info() const;
    
	// -----------------------------------------------------------------------
    void set_user_info(void* value);
};

} // namespace Dereferee

#endif // DEREFEREE_ALLOCATION_INFO_IMPL_H
