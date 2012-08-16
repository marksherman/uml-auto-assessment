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

#ifndef WONT_COMPILE_TESTS_H
#define WONT_COMPILE_TESTS_H

#include <dereferee.h>

/*
 * This test suite bundles all of the test cases that should not compile if
 * everything in Dereferee is working properly. Uncomment the appropriate line
 * in all_tests.h to verify that the compiler does indeed refuse them. 
 */
class wont_compile_tests
{
public:
	void dummy()
	{
		/*
		 * Initialization between pointers of incompatible types.
		 */
		{
			checked(int*) x = NULL;
/* 39 */	checked(char*) y = x;
		}
	
		/*
		 * Assignment between pointers of incompatible types.
		 */
		{
			checked(int*) x = NULL;
			checked(char*) y = NULL;
/* 48 */	x = y;
		}
		
		/*
		 * Comparisons between pointers of incompatible types.
		 */
		{
			checked(int*) x = NULL;
			checked(char*) y = NULL;
/* 57 */	x == y;
/* 58 */	x != y;
/* 59 */	x < y;
/* 60 */	x <= y;
/* 61 */	x > y;
/* 62 */	x >= y;
		}
		
		/*
		 * Various const-ness tests.
		 */
		{
			checked(int*) p1 = new int(1);
			checked(const int*) cp1 = new int(3);
			checked(int* const) pc1 = new int(5);
			checked(const int* const) cpc1 = new int(10);

			checked(int*) a1 = new int[1];
			checked(const int*) ca1 = new int[1];
			checked(int* const) ac1 = new int[1];
			checked(const int* const) cac1 = new int[1];

			a1[0] = 30;
/* 80 */	ca1[0] = 30;	// should not compile
			ac1[0] = 30;
/* 82 */	cac1[0] = 30;	// should not compile

			*p1 = 1;
/* 85 */	*cp1 = 3;		// should not compile
			*pc1 = 5;
/* 87 */	*cpc1 = 10;		// should not compile

/* 89 */	p1 = cp1;		// should not compile
			p1 = pc1;
/* 91 */	p1 = cpc1;		// should not compile
			
			cp1 = p1;
			cp1 = pc1;
			cp1 = cpc1;
			
/* 97 */	pc1 = p1;		// should not compile
/* 98 */	pc1 = cp1;		// should not compile
/* 99 */	pc1 = cpc1;		// should not compile
			
/* 101 */	cpc1 = p1;		// should not compile
/* 102 */	cpc1 = pc1;		// should not compile
/* 103 */	cpc1 = cp1;		// should not compile

			delete p1;
			delete cp1;
			delete pc1;
			delete cpc1;

			delete[] a1;
			delete[] ca1;
			delete[] ac1;
			delete[] cac1;
		}
	}
};

#endif // WONT_COMPILE_TESTS_H
