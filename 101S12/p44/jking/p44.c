/****************************************************************/
/* Programmer: Jared King                                       */
/* Program 44: Linked List                                      */
/* Approx Completion Time: 2 Hours                              */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_entered( NODE* start ) ;
void print_reverse( NODE* start ) ;
int main (int argc, char* argv[]){

  NODE* start = NULL;
  float data;
  int test;

  printf("Enter data: ");
  test=scanf("%f",&data);
  if (test !=EOF){
    while (test !=EOF){
      start=insert_node(data,start);
      printf("Enter data: ");
      test=scanf("%f", &data);
    }

    printf ("\nYour data was stored as:\n");
    print_entered(start);
    printf ( "\nYour data was entered as:\n");
    print_reverse(start);
    free_list(start);
  }
 
  putchar ('\n');
  return 0;
}

void print_entered(NODE* start){

  printf("%f\n", start->data);
  while(start->next != NULL){
    start = start->next;
    printf("%f\n" , start->data);
  }
}

void print_reverse( NODE* start ){

  if (start->next == NULL)
    printf ("%f\n", start->data);
    else {
      print_reverse(start->next);
      printf("%f\n", start->data);
  }
}
