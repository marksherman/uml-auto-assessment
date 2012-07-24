/****************************************************************/
/* Programmer: Dylan Bochman                                    */
/*                                                              */
/* linked_list.c: Provides linked list tools                    */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T, defined in linked_list.h  */
/*                                                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

/* make_node - stores data in a node and returns a pointer to it*/

NODE* make_node( DATA_T new_data, NODE* new_next ){
    NODE* newnode;
    newnode = (NODE*)malloc( sizeof(NODE) );
    newnode -> next = new_next;
    newnode -> data = new_data;
    return newnode;
}

/* insert_node - adds a node and returns a pointer to its predecessor */

NODE* insert_node( DATA_T new_data, NODE* start ){
    NODE* newstart;
    newstart = make_node( new_data, start );
    return newstart;
}

/* free_data - frees the allocated memory*/


void free_data( NODE* start ){

    if (start -> next != NULL ){
        free_data( start -> next );
	free( start );
    }
    else {
	free ( start );
    }
}

/* original - prints data in original order */

void original( NODE* start ){

    if (start -> next != NULL ){
        original( start -> next );
        printf( "%.3f\n", start -> data );
    }
    else {
        printf( "%.3f\n", start -> data );
    }
}


/* reverse - prints data in reverse order*/

void reverse( NODE* start ){
    
    if ( start -> next != NULL ){
	printf( "%.3f\n" , start -> data );
	return reverse( start -> next );
    }
    printf( "%.3f\n" , start -> data );
    return;
}





