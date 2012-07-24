/****************************************************************/
/* Programmer:Betty Makovoz                                     */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time:50 minutes                           */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[]){
  NODE* start = NULL;
  DATA_T new_data;
  
  printf( "Please enter numbers: \n" );

  while( scanf( "%f" , &new_data ) != EOF )
    start = insert_node( new_data , start );
  
  printf( " The list of numbers forward:\n" );
  print_forward( start );

  printf("\n");

  printf( " The list of numbers backwards:\n" );
  print_backward( start );
  
  printf( "\n" );
  
  free_list( start );
  
  return 0;
}   
