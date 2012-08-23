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
#include <dereferee/allocation_info_impl.h>

namespace Dereferee
{

// ---------------------------------------------------------------------------
allocation_info_impl::allocation_info_impl(mem_info& i) : info(&i)
{
}

// ---------------------------------------------------------------------------
const void* allocation_info_impl::address() const
{
	return info->address;
}

// ---------------------------------------------------------------------------
size_t allocation_info_impl::block_size() const
{
	return info->block_size;
}

// ---------------------------------------------------------------------------
bool allocation_info_impl::is_array() const
{
	return info->is_array;
}

// ---------------------------------------------------------------------------
size_t allocation_info_impl::array_size() const
{
	return info->array_size;
}

// ---------------------------------------------------------------------------
const char* allocation_info_impl::type_name() const
{
	return info->type_name;
}

// ---------------------------------------------------------------------------
void** allocation_info_impl::backtrace() const
{
	return info->backtrace;
}

// ---------------------------------------------------------------------------
void* allocation_info_impl::user_info() const
{
	return info->user_info;
}

// ---------------------------------------------------------------------------
void allocation_info_impl::set_user_info(void* value)
{
    info->user_info = value;
}

} // namespace Dereferee
