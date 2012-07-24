/****************************************************************/
/* Programmer: Lisa Mayers                                      */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 3 hours                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_backwards( NODE* n){
  if (n == NULL) return;
  print_backwards(n -> next);
  printf("%.5f\n", n -> data);
 
}

void print_forward(NODE* n ){
  while(n != NULL){
    printf("%.5f\n", n -> data);
    n = n -> next;
  }
  return;
}

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	float x;

	printf("Please insert a float:\n");

	while(scanf("%f", &x)!= EOF){
	  start = insert_node(x,start);
	  printf("Please insert a float:\n"); 	
	}

	print_forward( start ) ;
	print_backwards( start );
	free_list(start);
	
	return 0;
}
