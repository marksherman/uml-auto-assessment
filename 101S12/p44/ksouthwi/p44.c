/****************************************************************/
/* Programmer: Kevin Southwick                                  */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 3 hours                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
        NODE* start = NULL;
	DATA_T new_data;
	
	printf( "Inuput a bunch of numbers, end with C-d.\n" );

	while( scanf( "%f" , &new_data ) != EOF )
	  start = insert_node( new_data , start );

	printf( "Printing your list backwards.\n" );
	print_reverse( start );

	printf( "and forwards.\n" );
	print_forward( start );

	printf( "\n" );

	free_list(start);
	

	return 0;
}
