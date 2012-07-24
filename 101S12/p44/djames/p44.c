/****************************************************************/
/* Programmer: Dalton James                                     */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: hour and a half                     */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_f( NODE* start );
void print_b( NODE* start );

int main (int argc, char* argv[]){
 
  NODE* start = NULL;
	
  DATA_T data;
    
  printf( "Enter numbers ending in an EOF\n" );

  while( scanf( "%f", &data ) != EOF )

    start = insert_node( data, start );

  putchar( '\n' );

  printf( "The numbers entered in order are:\n" );

  print_f( start );
  
  putchar( '\n' );

  printf( "The numbers entered in reverse order are:\n" );

  print_b( start );
  
  putchar( '\n' );

  free_list(start);
  
  return 0;
}

void print_f( NODE* start ){
  
  if( start -> next == NULL ){
    
    printf( "%f ", start -> data );
    
  }else{
    
    print_f( start -> next );
    
    printf( "%f ", start -> data );
  }
  return;
}

void print_b( NODE* start ){

  if( start -> next == NULL ){

    printf( "%f ", start -> data );

  }else{

    printf( "%f ", start -> data );

    print_b( start -> next );
  }
  return;
}
