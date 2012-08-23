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

#ifndef DEREFEREE_POINTER_TRAITS_H
#define DEREFEREE_POINTER_TRAITS_H

namespace Dereferee
{
	
// ===========================================================================
/**
 * The pointer_traits class and its specializations are used to "pull apart"
 * a pointer type to determine the value type underlying it, and to provide
 * convenience typedefs for the pointer and reference types of a particular
 * type T.
 */

template <typename T>
struct pointer_traits { };

template <typename T>
struct pointer_traits<T*>
{
	typedef T value_type;
	typedef T* pointer_type;
	typedef const T* const_pointer_type;
	typedef T& reference_type;
	typedef const T& const_reference_type;
};

template <typename T>
struct pointer_traits<T* const>
{
	typedef T value_type;
	typedef T* const pointer_type;
	typedef const T* const const_pointer_type;
	typedef T& reference_type;
	typedef const T& const_reference_type;
};

}

#endif // DEREFEREE_POINTER_TRAITS_H
