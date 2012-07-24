/****************************************************************/
/* Programmer: Thomas Frank                                     */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 120 Minutes                         */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_value(NODE* current);
void print_back(NODE* current);

int main (int argc, char* argv[])
{
	DATA_T value;
	NODE* start = NULL;
	NODE* node_list;
	int i = 1;
	
	while(i != EOF){
		printf("Please enter a float, or EOF\n");
		i = scanf("%f", &value);
		if(i != EOF){
			start = insert_node(value, start);
		}
	}
	printf("\nPrint List as stored\n");
	node_list = start;
	while(node_list != NULL){
		print_value(node_list);
		node_list = node_list->next;
	}

	printf("\nPrint List as entered\n");
	node_list = start;
	print_back(node_list);
	free_list(start);
	
	return 0;
}

void print_value(NODE* current){
	printf("%f\n", current->data);
}

void print_back(NODE* current){

	if(current->next != NULL){
		print_back(current->next);
		print_value(current);
	}
	else{
		print_value(current);
	}
}

