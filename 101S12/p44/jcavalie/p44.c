/****************************************************************/
/* Programmer: John Cavalieri                                   */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time:  50min                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	DATA_T input;
	int end_check;
	
	printf( "\tEnter data points; end with EOF\n" );

	while ( ( end_check = scanf( "%f", &input )) != EOF ){

		
		start = insert_node( input , start );
	}
	putchar('\n');
	
	printf( "\n\tList in reverse\n" );

	print_revs( start );
	
	printf( "\tList unreversed\n" );
	
	print_norm( start );

	free_list( start );
	
	return 0;
}
