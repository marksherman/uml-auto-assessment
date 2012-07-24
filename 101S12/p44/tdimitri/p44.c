/****************************************************************/
/* Programmer: Theodore Dimitriou                               */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 7 hours                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
  NODE* start = NULL;

  NODE *cur, *new_next;
  DATA_T new_data = 0, data = 0;

  cur = start;

  printf( "Enter integers until EOF: " );
  while ( scanf( "%f", &data ) != EOF ){
    start = insert_node( data, start );
    printf( "Enter data: " );
  }
  putchar( '\n' );
  originalprint( start );
  putchar( '\n' );
  free_list( start );
  
  return 0;
}
