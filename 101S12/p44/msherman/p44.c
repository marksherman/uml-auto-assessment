/****************************************************************/
/* Programmer: Tyler Gilzinger                                  */
/*                                                              */
/* Program 50: Non-polymorphic Linked List                      */
/*                                                              */
/* Program Completion Time: 35 minutes                          */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linkedlist.h"

int main (int argc, char* argv[])
{
	int input;
	NODE* start = 0;
	NODE* currnode;

	printf("Enter an integer, or EOF to end the linked list: ");
	while (scanf("%d", &input) != EOF) {
		if (start != 0)
			start = insertnode(input, start);
		else
			start = makenode(input);

		printf("\nEnter an integer, or EOF to end the linked list: ");
	} 
	
	currnode = start;
 
	printf("\n\n");

	while (currnode != '\0') {
		printf("%d ", currnode->data);
		currnode = currnode->next;
	}

	printf("\n\n");
	
	return 0;
}
