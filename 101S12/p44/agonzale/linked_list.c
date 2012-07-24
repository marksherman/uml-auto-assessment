/****************************************************************/
/* Programmer: Alexander Gonzalez                               */
/*                                                              */
/* linked_list.c: Provides linked list tools                    */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T, defined in linked_list.h  */
/*                                                              */
/****************************************************************/

#include <stdlib.h>
#include "linked_list.h"

NODE* make_node( DATA_T new_data, NODE* new_next ){
	NODE* newnode;
	
	newnode = (struct node*) malloc( sizeof(struct node) );
	
	newnode->next = NULL;
	newstart->next = newnode;

	return newnode;
}


NODE* insert_node( DATA_T new_data, NODE* start ){
	NODE* newstart;
		
		
	return newstart;
}

void free_list( NODE* start ){

    if( start->next == NULL)
	free(start);
    else {
	free_list(s->next);
	free(start);
    }
}
