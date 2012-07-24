/****************************************************************/
/* Programmer: Zachary Robichaud                                */
/*                                                              */
/* Program 44: Linked List                                      */
/*                                                              */
/* Program Completion Time: 4 hours                             */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void print_entered( NODE* start ) ;

void print_reverse( NODE* start ) ;

int main ( int argc, char* argv[] ) {

	NODE* start = NULL ;
	float data ;
	int test ;

	/* create first node */
	printf ( "Enter new data : " ) ;
	test = scanf ( "%f" , &data ) ;
	/* if first input if EOF skips the loop and also all calling functions
	   so that it doesn't seg fault. */
	if ( test != EOF ) {
		/* makes rest of nodes until EOF */
		while ( test != EOF ) {		
			start = insert_node( data , start ) ;
			printf ( "Enter new data : " ) ;
			test = scanf ( "%f" , &data ) ;	       		
		}
		/* print data entered */
		printf ( "\nHere is your data as it was stored :\n" ) ;
		print_entered( start ) ;
		printf ( "\nHere is your data as it was entered :\n" ) ;
		print_reverse( start ) ;			
		/* free memory */
		free_list( start ) ;
	}	
	/* make it look pretty */
	putchar ( '\n' ) ;	
	return 0 ;
}

void print_entered( NODE* start ) {
		
	/* print in reverse of order entered. prints first data in event that
	 only one node is entered */
	printf ( "%f\n" , start->data ) ; 
	while ( start->next != NULL ) {  
		start = start->next ; 
		printf ( "%f\n" , start->data ) ;	       
	}
}

void print_reverse( NODE* start ) {

	/* prints data in order entered. If it is last node than print right 
	   away otherwise call yourself until you get to node with NULL and 
	   then print back up the recursion */
	if ( start->next == NULL ) 
		printf ( "%f\n" , start->data ) ;
	else {
		print_reverse ( start->next ) ;	
		printf ( "%f\n" , start->data ) ;
	}	
}
