/****************************************************************/
/* Programmer: Alexander Gonzalez                               */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 5+ hours                            */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	
	linked_list * vptr;
	ptr = (linked_list*) malloc( sizeof(linked_list) );

        NODE* cur;
	
	float start;
	int x, y;
	int num;

	printf("Enter any amount of integers:\n");

	for(x=0; x<start; x++) {
	    scanf("%f",&num);
	}

	cur = start
	    while (cur->next != NULL) {
		cur = cur->next;
	    }

	for ( x = 999; x>=0; x--){
	    printf("The numbers in reverse are: %d\n", num);
	}
	
	for ( y = 0; y < x; y++){
	    printf("The numbers in un-reversed order are: %d\n", num);
	}
		
	free_list(start);
	
	return 0;
}
