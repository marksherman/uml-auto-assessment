/****************************************************************/
/* Programmer: Joshua Gorham                                    */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: hour or so (non consecutive)        */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void reverse_print( NODE* start );
void forward_print( NODE* start );

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	DATA_T input = 0;


	printf("Enter Numbers to store: \n");

	while( scanf("%f", &input) != EOF)
	  start = insert_node( input, start );

	printf("\n");
	forward_print( start );
	printf("\n");
	reverse_print( start );
	
	free_list(start);
	
	return 0;
}


void reverse_print( NODE* start ){
  if(start -> next == NULL)
    printf("%f\n", start -> data);
  else{
    reverse_print(start -> next);
    printf("%f\n", start -> data);
  }
}

void forward_print( NODE* start ){
  if(start -> next != NULL){
    printf("%f\n", start -> data);
    forward_print( start -> next );
  }
  else
    printf("%f\n", start -> data);
}
