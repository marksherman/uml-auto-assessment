/****************************************************************/
/* Programmer: Jared King                                       */
/****************************************************************/

#include <stdlib.h>
#include "linked_list.h"

NODE* make_node(DATA_T new_data, NODE* new_next){

  NODE* newnode;

  newnode=(NODE*)malloc(sizeof(NODE));
  newnode->data=new_data;
  newnode->next=new_next;

  return newnode;
}

NODE* insert_node(DATA_T new_data, NODE* start){
  
  NODE* newstart;
  newstart=make_node(new_data,start);
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
