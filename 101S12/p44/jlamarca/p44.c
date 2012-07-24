/****************************************************************/
/* Programmer: Joe LaMarca                                      */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 3 days                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_unreversed(float a);

int main (int argc, char* argv[])
{
	NODE* start = NULL;
	DATA_T x;
	NODE* i;
	
	i=0;
	while(x!=EOF){
	  printf("Enter a number:");
	  scanf("%f", &x);
	  insert_node(x, i);
	  i++;
	  print_unreversed(x);
	  
	}

	free_list(start);
	
	return 0;
}

NODE* make_node(DATA_T new_data, NODE* new_next){

  if(new_next->next==NULL)
    printf("%f", new_data);
  else
    make_node(new_data, new_next);

  return new_next;
}

NODE* insert_node(DATA_T new_data, NODE* start){

  NODE* cur;

  cur=start;
  while(cur->next!=NULL)
    cur=cur->next;

  return start;
 
}


void free_list(NODE* start){

  if(start->next==NULL)
    free(start);
  else{
    free_list(start->next);
    free(start);
  }
}


void print_unreversed(float a){

  printf("%f", a);

}
