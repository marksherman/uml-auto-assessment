/****************************************************************/
/* Programmer: Joanna Sutton                                    */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time:  3-4 hours                          */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int print(NODE* start);
int printrev(NODE* start);
int main (int argc, char* argv[]){
  DATA_T i;
  NODE* start = NULL;

  printf("Please enter a single integer.\n");
  
  while(scanf("%f",&i)!=EOF)
    start=insert_node(i,make_node(i,start));

/*  print(start);*/
  printrev(start);


  return 0;

  free_list(start);

}

int print(NODE* start){

  while(start!=NULL){
    printf("%f\n",start->data);
    start=start->next;
  }
  return 0;
}

int printrev(NODE* start){
  NODE*  tmp2;
  NODE* tmp;
  
  tmp2=NULL;
  while(start!=NULL){
    tmp=tmp2;
    tmp2=start;
    start=start->next;
    tmp2->next=tmp;
  }
  start=tmp2;

  while(start!=NULL){
    printf("%f\n",start->data);
    start=start->next;
  }
  return 0;
}


