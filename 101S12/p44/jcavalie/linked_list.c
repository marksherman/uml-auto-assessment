/****************************************************************/
/* Programmer:  John Cavalieri                                  */
/*                                                              */
/*  linked_list.c: Provides linked list tools                   */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T, defined in linked_list.h  */
/*                                                              */
/****************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"


NODE* make_node( DATA_T new_data, NODE* new_next ){
	NODE* newnode;
	

	newnode = (NODE*)malloc( sizeof(NODE) );
	newnode -> data = new_data;
	newnode -> next = new_next;
	

	return newnode;
}


NODE* insert_node( DATA_T new_data, NODE* start ){
	NODE* newstart;
	
	newstart = make_node( new_data, start );
	
		
	return newstart;
}


void free_list( NODE* start ){


	if (start -> next == NULL ){
		free( start );
	}
	else{
		free_list( start -> next );
		free( start );
	}
	
}


void print_revs( NODE* start ){
	
	if ( start -> next == NULL ){
		printf( "%f\n" , start -> data );
		return;
	}

	printf( "%f\n" , start -> data );

	return print_revs( start -> next );
      
}

	
	
void print_norm( NODE* start ){


        if (start -> next == NULL ){
		printf( "%f\n", start -> data );
                
	}
        else{
		print_norm( start -> next );
                printf( "%f\n", start -> data );
		
	}

}
