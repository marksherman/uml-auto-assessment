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

#ifdef _MSC_VER
#pragma warning(push)
#pragma warning(disable:4611)
#endif

#include "initialization_assignment_tests.h"
#include "memory_leak_tests.h"
#include "comparison_tests.h"
#include "const_correctness_tests.h"
#include "array_indexing_tests.h"
#include "polymorphism_tests.h"
#include "new_operator_tests.h"
#include "delete_operator_tests.h"
#include "dynamic_cast_tests.h"
#include "const_cast_tests.h"
#include "arithmetic_tests.h"
#include "no_default_ctor_tests.h"
#include "abstract_class_tests.h"

// Uncomment this line to verify that the compiler refuses illegal code for
// checked pointers that it would for raw pointers.

// #include "wont_compile_tests.h"

#ifdef _MSC_VER
#pragma warning(pop)
#endif
