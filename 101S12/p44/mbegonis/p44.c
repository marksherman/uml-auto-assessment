/****************************************************************/
/* Programmer: Mike Begonis                                     */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: forever (about 3 hours)             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[]){
  
  NODE* ptr = NULL;
  DATA_T num;
  
  printf("\nPlease enter as many numbers as you want: ");
  
  while(scanf("%f", &num)!=EOF){
    ptr=insert_node(num,ptr);
  }
  
  printf("\n\nThe numbers you entered in the order you stored them is, \n");
  print_forward(ptr);
  printf("\n");
  
  printf("\nThe numbers you entered in reverse order is, \n");
  print_reverse(ptr);
  
  free_list(ptr);
  
  return 0;
}
