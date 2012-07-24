/****************************************************************/
/* Programmer: Brian McClory                                    */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 30 Minutes                          */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linkedList.h"

int main (int argc, char* argv[]){

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

  freeList(start);

  return 0;

}

void freeList(NODE* start){

  if(start != 0){

    start = *(start - 1);

    free(start);

    void freeList(NODE* (start));

  }

  else{

    return;
  }
 
}
