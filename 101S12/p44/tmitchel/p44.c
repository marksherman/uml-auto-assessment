/****************************************************************/
/* Programmer: Thomas Mitchell                                  */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 3 hours (very confused)             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
	NODE* start = NULL ;
	int num = 0 ;
	NODE* prnt ; 

	printf( "Enter integers to input into the Linked List\n" ) ;
	while ( scanf( "%d" , &num ) != EOF ) {
	  insert_node( num , start ) ; 
	    }

	prnt = start ;
	while ( prnt!=NULL ) {
	  printf( "%d\n" , prnt->next ) ;
	  prnt = prnt->next;
	}

	free_list(start);
	
	return 0;
}

