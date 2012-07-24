/****************************************************************/
/* Programmer: Brian McClory                                    */
/*                                                              */
/* linked_list.c: Provides linked list tools                    */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T, defined in linked_list.h  */
/*                                                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linkedList.h"

NODE* start = NULL;

NODE* makeNode(DATA_T newData, NODE* newNext){
  NODE* newNode;

  newNode = malloc(sizeof(NODE));

  newNode -> data = newData;
   
  newNode -> next = newNext;

  return newNode;
}


NODE* insertNode(DATA_T newData, NODE* start){
  NODE* newStart;

  newData -> start = newStart;
  
  return newStart;
}

void freeList(NODE* start){

  if(start == NULL || start == EOF){

    return;

  }
  
  else{

    void freeList(NODE* (start + 1));
  
    free(start - 1);

  }
}
