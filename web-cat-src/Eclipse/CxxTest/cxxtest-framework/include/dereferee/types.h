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

#ifndef DEREFEREE_TYPES_H
#define DEREFEREE_TYPES_H

namespace Dereferee
{

/**
 * The tag associated with a block of allocated memory is an ad hoc "timestamp"
 * that differentiates allocations that happen to use the same address but at
 * different times.
 */
typedef unsigned long memtag_t;

/**
 * The type used to keep track of the reference count for a block of memory.
 */
typedef unsigned long refcount_t;

/**
 * The default tag used for uninitialized pointers.
 */
const memtag_t default_memtag = (memtag_t)~0;

} // namespace Dereferee


#endif // DEREFEREE_TYPES_H
