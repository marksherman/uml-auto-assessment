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

#ifndef DEREFEREE_USAGE_STATS_IMPL_H
#define DEREFEREE_USAGE_STATS_IMPL_H

#include "../dereferee/listener.h"
#include <cstdarg>

namespace Dereferee
{

// ===========================================================================
/**
 * A concrete implementation of the Dereferee::usage_stats interface, used by
 * the memory manager to report memory usage statistics to the listener.
 */
class usage_stats_impl : public usage_stats
{
private:
	unsigned int _leaks;
	size_t _total_bytes_allocated;
	size_t _current_bytes_allocated;
	size_t _maximum_bytes_in_use;
	size_t _calls_to_new;
	size_t _calls_to_delete;
	size_t _calls_to_array_new;
	size_t _calls_to_array_delete;
	size_t _calls_to_delete_null;
	size_t _calls_to_array_delete_null;
	
public:
	usage_stats_impl();
	
	size_t leaks() const;
	size_t total_bytes_allocated() const;
	size_t maximum_bytes_in_use() const;
	size_t calls_to_new() const;
	size_t calls_to_array_new() const;
	size_t calls_to_delete() const;
	size_t calls_to_array_delete() const;
	size_t calls_to_delete_null() const;
	size_t calls_to_array_delete_null() const;
	
	void set_leaks(size_t leaks);
	void record_allocation(size_t size, bool is_array);
	void record_deallocation(size_t size, bool is_array);
	void record_null_deallocation(bool is_array);
};

} // namespace Dereferee

#endif // DEREFEREE_USAGE_STATS_IMPL_H
