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

/*
 * ACKNOWLEDGMENTS:
 * This code is based on and extends the work done by Scott M. Pike and
 * Bruce W. Weide at The Ohio State University and Joseph E. Hollingsworth of
 * Indiana University SE in "Checkmate: Cornering C++ Dynamic Memory Errors
 * With Checked Pointers", Proc. of the 31st SIGCSE Technical Symposium on
 * CSE, ACM Press, March 2000.
 */

#include <cstdlib>
#include <dereferee/usage_stats_impl.h>

namespace Dereferee
{

// ===========================================================================

usage_stats_impl::usage_stats_impl()
{
	_total_bytes_allocated = 0;
	_current_bytes_allocated = 0;
	_maximum_bytes_in_use = 0;
	_calls_to_new = 0;
	_calls_to_delete = 0;
	_calls_to_array_new = 0;
	_calls_to_array_delete = 0;
	_calls_to_delete_null = 0;
	_calls_to_array_delete_null = 0;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::leaks() const
{
	return _leaks;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::total_bytes_allocated() const
{
	return _total_bytes_allocated;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::maximum_bytes_in_use() const
{
	return _maximum_bytes_in_use;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_new() const
{
	return _calls_to_new;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_array_new() const
{
	return _calls_to_array_new;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_delete() const
{
	return _calls_to_delete;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_array_delete() const
{
	return _calls_to_array_delete;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_delete_null() const
{
	return _calls_to_delete_null;
}

// ---------------------------------------------------------------------------
size_t usage_stats_impl::calls_to_array_delete_null() const
{
	return _calls_to_array_delete_null;
}

// ---------------------------------------------------------------------------
void usage_stats_impl::set_leaks(size_t leaks)
{
	_leaks = leaks;
}

// ---------------------------------------------------------------------------
void usage_stats_impl::record_allocation(size_t size, bool is_array)
{
	if(is_array)
		_calls_to_array_new++;
	else
		_calls_to_new++;

	_current_bytes_allocated += size;
	_total_bytes_allocated += size;
	
	if(_current_bytes_allocated > _maximum_bytes_in_use)
		_maximum_bytes_in_use = _current_bytes_allocated;
}

// ---------------------------------------------------------------------------
void usage_stats_impl::record_deallocation(size_t size, bool is_array)
{
	_current_bytes_allocated -= size;
	
	if(is_array)
		_calls_to_array_delete++;
	else
		_calls_to_delete++;
}

// ---------------------------------------------------------------------------
void usage_stats_impl::record_null_deallocation(bool is_array)
{
	if(is_array)
		_calls_to_array_delete_null++;
	else
		_calls_to_delete_null++;
}


} // namespace Dereferee
