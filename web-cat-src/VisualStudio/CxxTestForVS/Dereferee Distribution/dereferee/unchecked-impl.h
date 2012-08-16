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

namespace Dereferee
{

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::checked_ptr()
{
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::checked_ptr(pointer_type ptr) :
	pointer(ptr)
{
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const dynamic_cast_helper<U*>& rhs) :
	pointer((pointer_type)rhs)
{
}

// -----------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const dynamic_cast_helper<U* const>& rhs) :
	pointer((pointer_type)rhs)
{
}

// -----------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(const dynamic_cast_helper<U*>& rhs)
{
	pointer = (pointer_type)rhs;
	return *this;
}

// -----------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(
	const dynamic_cast_helper<U* const>& rhs)
{
	pointer = (pointer_type)rhs;
	return *this;
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator==(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer == rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator!=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return !(lhs == rhs);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator<(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer < rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator<=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer <= rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator>(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer > rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator>=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer >= rhs.pointer);
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator++()
{
	pointer++;
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T> checked_ptr<T>::operator++(int)
{
	checked_ptr<T> retval = *this;
	++(*this);
	return retval;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator--()
{
	pointer--;
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T> checked_ptr<T>::operator--(int)
{
	checked_ptr<T> retval = *this;
	--(*this);
	return retval;	
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator+(const checked_ptr<U>& ptr, ptrdiff_t delta)
{
	return checked_ptr<U>(ptr.pointer + delta);
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator+(ptrdiff_t delta, const checked_ptr<U>& ptr)
{
	return (ptr + delta);
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator-(const checked_ptr<U>& ptr, ptrdiff_t delta)
{
	return (ptr + -delta);
}

// ------------------------------------------------------------------
template <typename U, typename V>
ptrdiff_t operator-(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return (lhs.pointer - rhs.pointer);
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator+=(ptrdiff_t delta)
{
	pointer += delta;
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator-=(ptrdiff_t delta)
{
	return (*this += -delta);
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::reference_type checked_ptr<T>::operator*()
{
	return *pointer;
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::pointer_type checked_ptr<T>::operator->()
{
	return pointer;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::operator pointer_type() const
{
	return pointer;
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::operator checked_ptr<U>()
{
	return checked_ptr<U>(pointer);
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::reference_type checked_ptr<T>::operator[](int index)
{
	return pointer[index];
}


} // namespace Dereferee
