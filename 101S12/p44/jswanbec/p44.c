/****************************************************************/
/* Programmer: Jimmy Swanbeck                                   */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 120 minutes                         */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void backwards (NODE*);

int main (int argc, char* argv[])
{
  float x;

  NODE* start = NULL;
  while( x != EOF )
    {
      printf( "Enter a number: " );
      scanf( "%f" , &x );
      if( x != EOF )
	{
	start = insert_node( x , start );
	}
    }

  NODE* n = start;
  while( n != NULL )
    {
      printf( "%f " , n->data );
      n = n->next;
    }
  printf( "\n" );

  backwards( start );
  printf( "\n" );

  free_list(start);
  return 0;
}

void backwards (NODE* n)
{
  if( n != NULL )
    {
      backwards( n->next );
      printf( "%f " , n->data );
    }
}
