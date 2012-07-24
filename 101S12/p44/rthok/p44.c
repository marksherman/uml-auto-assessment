/****************************************************************/
/* Programmer: Ravy Thok                                        */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 150 mintues (didn't finish)         */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[]){

	NODE* start = NULL;

	float data = 0 ;

	printf( "Enter a number: " ) ;

	while( scanf( "%f", &data ) != EOF  ){

		start = insert_node( data , start ) ;

		printf( "\nEnter another number or EOF( twice ): " ) ;

	}

	printf( "\n%f\n", start -> data ) ;

	free_list( start );
	
	return 0;
}
