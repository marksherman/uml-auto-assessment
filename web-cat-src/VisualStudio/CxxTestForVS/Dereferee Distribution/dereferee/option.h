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

#ifndef DEREFEREE_OPTION_H
#define DEREFEREE_OPTION_H


namespace Dereferee
{

// ===========================================================================
/**
 * An array of these structures is passed into the create_listener and
 * create_platform functions so that users can customize the behavior of the
 * listener and platform at compile-time or at runtime.
 */
struct option
{
	const char* key;
	const char* value;
};

} // namespace Dereferee

#endif // DEREFEREE_OPTION_H
