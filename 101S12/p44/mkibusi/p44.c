/****************************************************************/
/* Programmer:                                                  */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time:                                     */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void forward_start(NODE* ptr);
void reverse_start(NODE* ptr);

int main (int argc, char* argv[]){
  
  NODE* start = NULL;
  NUM i;
  NODE num;
  
  DATA_T y, x;

  printf("Please enter the first number \n");
  start -> next = NULL;

  printf("Please enter the next number \n");
  i = 1;
  while( (num -> next) != EOF){
    num = num -> next;
    
    i++;
  }

  x = make_node(num, start);
  y = insert_node(num, start);

  printf("THe number is %f\n", x);
  printf("THe number is %f\n", y);
  }
  
  forward_start(start);
  reverse_start(start);
  
  free_list(start);
  
  return 0;
}
void forward_start(NODE* ptr){
 
  NUM i = 1;
  printf("\n\n");
  while(ptr != NULL){
    printf(" The number = %f\n",ptr -> data);
    i++;
    ptr -> next = data;
  }
}
void reverse_start(NODE* ptr){
  NUM i= 1;
  while(ptr == NULL){
    printf("The reverse number is %f \n ", --ptr);
    i--;
    ptr -> next = NULL;
  }
  
}
