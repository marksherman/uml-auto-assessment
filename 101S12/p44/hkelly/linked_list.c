/****************************************************************/
/* Programmer: Harrison Kelly                                   */
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
  
  newnode = (NODE*)malloc(sizeof(DATA_T));
  newnode->data = new_data; /* Puts new data/null pointer in new node. */
  newnode->next = new_next;
  return newnode; /* Returns pointer to where the created node is. */
}

NODE* insert_node( DATA_T new_data, NODE* start ){
  NODE* newstart;
  
  newstart = make_node( new_data, NULL ); /* Creates a new node */
                                          /* that points to NULL */
  newstart->next = start; /* changes the pointer of new node to start */
                          /* which makes it point to the old starting */
                          /* node, this is inserted before it         */
  return newstart;
}

void free_list( NODE* start ){

  if( start -> next == NULL ){ /* If the next pointer is null, free next node*/
    free( start );
  }
  else{
    free_list( start -> next ); /* Rerun function seeing if next node is NULL*/
    free( start );
  }
}

void print_list_forwards( NODE* start ){
  NODE* current;
  current = start; /* Temp variable to hold location of starting point */
  
  printf("%f\n", current -> data ); /* Prints first node before shift */
  while( current -> next != NULL ){
    current = current -> next; /* Switches current to next node and prints */
    printf("%f\n", current -> data);
  }
  
}
  
void print_list_backwards( NODE* start ){ /* Same as freeing memory */
  
  if( (start -> next == NULL) ){
    printf("%f", start -> data);
  }
  else{
    print_list_backwards( start -> next );
    printf("\n%f", start -> data);
  }

}

