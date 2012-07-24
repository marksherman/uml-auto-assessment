/****************************************************************/
/* Programmer: Nathan Goss                                      */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 15 minutes                          */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_listRECURSE( NODE* currentnode );
void print_list( NODE* currentnode );

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	DATA_T inData;
	

	printf("Please input a floating point number: ");
	
	while(scanf("%f", &inData) != EOF)
	{
	    start = insert_node(inData, start);
	    printf("Please input a floating point number: ");
	}
	putchar('\n');

	print_listRECURSE(start);
	putchar('\n');

	print_list(start);
	putchar('\n');

	free_list(start);
	
	return 0;
}


void print_list( NODE* currentnode )
{
    while (currentnode -> next != NULL)
    {
	printf("%f ", currentnode -> data);
	currentnode = currentnode -> next;
    }
    printf("%f ", currentnode -> data);
}

void print_listRECURSE( NODE* currentnode )
{
    if(currentnode -> next == NULL)
	printf("%f ", currentnode -> data);
    else
    {
	print_listRECURSE( currentnode -> next );
	printf("%f ", currentnode -> data);
    }
}
