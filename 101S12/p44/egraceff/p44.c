/****************************************************************/
/* Programmer: Erin Graceffa                                    */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 2 hours                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"
void print_reverse(NODE* current2);
int main (int argc, char* argv[])
{
  	NODE* start = NULL;
	DATA_T x;
	NODE* current1;

	printf("Please enter a number:\n");
	while(scanf("%f", &x)!= EOF){
	  start = insert_node(x, start);
	  printf("Please enter another number:\n");
	}
	/* Continually prompts the user for a number until the user
	 * enters an EOF.
	 */
	
	current1 = start;
	while(current1 != NULL){
	  printf("%f ", current1 -> data);
	  current1 = current1 -> next;
	}
	printf("\n");
	/* Prints the data in the order it was stored (reverse):
	 * Lets the pointer to the first node equal current, then runs 
	 * the while loop under the condition that the pointer points to
	 * the next node, not NULL.
	 * If this is true, the data that the node 'current' points to is
	 * printed and 'current' is reassigned to be the pointer in the node
	 * that the data was just printed from. 
	 */

	print_reverse(start);
	/* calls the print_reverse function to print the values in the
	 * order they were originally entered*/
	printf("\n");
	free_list(start);
	/* frees memory allocated in the heap */
	return 0;
}

void print_reverse(NODE* current2){
  if(current2 -> next == NULL){
    printf("%f ", current2 -> data);
    return ;
  }
  else{
    print_reverse(current2 -> next);
    printf("%f ", current2 -> data);
  }

/* Prints the data in the order it was entered (original order):
 * Lets the pointer to the first node equal current2. The function
 * runs recursively. Once the base case is reached, being that the 
 * next pointer stored in current2 is NULL, the data stored in the
 * current2 node will be printed. Then, the function will kick back,
 * printing each piece of data from the last node to the first.
 */
}
