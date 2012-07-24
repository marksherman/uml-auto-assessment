/****************************************************************/
/* Programmer:                                                  */
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
	newnode=(NODE*)malloc(sizeof(NODE));
	newnode->next=new_next;
	newnode->data=new_data;
	return newnode;
}

NODE* insert_node( DATA_T new_data, NODE* start ){
	NODE* newstart;
	
	newstart->data=new_data;
	
	if(start==NULL){
	  start=newstart;
	  start->next=NULL;
	}
	else{
	  newstart->next=start;
	  start=newstart;
	}

	return newstart;
}

void free_list( NODE* start ){

  if(start->next==NULL)
    free(start);
  else{
    free_list(start->next);
    free(start);
  }

}
