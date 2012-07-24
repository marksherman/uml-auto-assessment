/****************************************************************/
/* Programmer: Dylan Bochman                                    */
/* Program 44: Linked List                                      */
/* Program Completion Time: 3 hours                             */
/****************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[]) {
    NODE* start = NULL;
    DATA_T data;
    int stop;

    printf( "\nEnter data now, When finished terminate via EOF\n" );

    while ( EOF != ( stop = scanf( "%f", &data )) ){
	start = insert_node( data , start );
    }

    printf( "\nReversed Data:\n" );

    reverse( start );

    printf( "\nOriginal Data:\n" );

    original( start );

    free_data(start);

    return 0;
}

