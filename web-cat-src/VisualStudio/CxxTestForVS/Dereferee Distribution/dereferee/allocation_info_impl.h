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
class allocation_info_impl : public allocation_info
{
private:
	const mem_info* info;

public:
	allocation_info_impl(const mem_info& i);
	
	const void* address() const;
	size_t block_size() const;
	bool is_array() const;
	size_t array_size() const;
	const char* type_name() const;
	void** backtrace() const;
};

} // namespace Dereferee

#endif // DEREFEREE_ALLOCATION_INFO_IMPL_H
