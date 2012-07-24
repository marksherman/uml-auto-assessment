/****************************************************************/
/* Programmer: Danny Packard                                    */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: All Day                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"
void print1(NODE*start);
void print2(NODE*start);
int main (int argc, char* argv[]){
 
  NODE*start=NULL;
  DATA_T x;
  printf("enter the list \n");
  while(scanf("%f",&x)!=EOF){
    start=insert_node(x,start);
  }
  printf("\n"); /* puts the printed list on new line from input*/
  print1(start);
  printf("\n");
  print2(start);
  printf("\n");
  free_list(start);
  return 0;
}
void print1(NODE*start){
  if(start->next==NULL)
    printf("  %f  ", start->data);
  else{
    printf("  %f  ", start->data);
    print1(start->next);
  }
/*printf \n works here but it leaves a huge gap between the two printed lists*/
}

void print2(NODE*start){
  if(start->next==NULL)
    printf("  %f  ",start->data);
  else{
    print2(start->next);
    printf ("  %f  ",start->data);
  }
 
}

