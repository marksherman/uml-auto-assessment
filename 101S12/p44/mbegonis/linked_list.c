/****************************************************************/
/* Programmer: Mike Begonis                                     */
/*                                                              */
/* linked_list.c: Provides linked list tools                    */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T, defined in linked_list.h  */
/*                                                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

/* make_node
 * Creates a new linked list node, which will contain:
 *  the data specified by "new_data" and
 *  the next pointer specified by "new_next".
 * Returns a pointer to the newly created node.
 */
NODE* make_node( DATA_T new_data, NODE* new_next ){

  NODE* newnode;
	
  newnode=(NODE*)malloc(sizeof(NODE));
  
  newnode->data=new_data;
  newnode->next=new_next;
  
  return newnode;
}

/* insert_node
 * Inserts a new node at the front of a linked list.
 * The current first node of the list is given by "start".
 * The data to be inserted into the new node is "new_data".
 * Returns a pointer to the newly created first node of the list.
 */
NODE* insert_node( DATA_T new_data, NODE* start ){

  NODE* newstart;
  
  newstart=make_node(new_data,start);
  
  return newstart;
}

/* free_list
 * Frees a linked list starting with node pointed to by "start".
 * Uses recursion to free the last node first, and the root node last.
 * How else could you do it? If you free the first node first, you
 *  lose the pointer to the rest of the list, and those nodes are leaked.
 */
void free_list( NODE* start ){
  
  if(start->next==NULL)
    free(start);
  else{
    free_list(start->next);
    free(start);
  }
}
/* print_forward
 * Prints out the numbers entered in the order they were entered.
 * It uses recursion to print the last node, containing the first
 * number first.  Then as each recursive call ends, it prints the 
 * number in each node.  Thus printing the entered numbers in order.
 */
void print_forward(NODE* ptr){
  if(ptr->next==NULL)
    printf("%f ",ptr->data);
  else{
    print_forward(ptr->next);
    printf("%f ",ptr->data);
  }
}

/* print_reverse
 * Prints out the numbers entered in a reversed order to that
 * of which they were entered.  It uses recursion to print out
 * the last number first.  It prints each number, then recursivly
 * calls the function again, thus printing the numbers contained
 * in the last nodes first, which prints the numbers in reverse order.
 */
void print_reverse(NODE* ptr){
  if(ptr->next==NULL)
    printf("%f \n\n",ptr->data);
  else{
    printf("%f ",ptr->data);
    print_reverse(ptr->next);
  }
}
