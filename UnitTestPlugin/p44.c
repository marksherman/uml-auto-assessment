/****************************************************************/
/* Programmer: Chris Leger                                      */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 90 Minutes                          */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	NODE* current;
	float datain;
	int nodecount;
	int i;


	printf( "Enter a floating point value or EOF:" );

	while( scanf( "%f", &datain ) != EOF ){
	  
	  start = insert_node( datain, start );
	  printf( "Enter a floating point value or EOF:" );
	  nodecount++;
	  
	}
	putchar( '\n' );
	
	printf( "In the order it was stored:\n" );
	current = start;
	
	for( i = 0; i < nodecount; i++ ){
	  
	  printf( "%f\n", current -> data );
	  current = current -> next;
	  
	}
	
	printf( "In the order it was entered:\n" );
	printdataforward( start );
	
	
	free_list(start);
	
	return 0;
}
