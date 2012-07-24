/********************************/
/* Author: James DeFilippo      */
/* Title : Bigger than 100      */
/* Approximate Time: 15 minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  int x; /* initialize an address for scanf to input data */ 
  printf( "Hi! Please enter a number.\n" ); /* prompt the user for meaningful input */  
  scanf("%d", &x); /* use standard input to write data to x's address */ 
  if (x>100) /* conditional statement */ 
    printf( "The number %d is bigger than 100.\n", x ); 
    else /* fall back if condition is not true */ 
      printf( "The number %d is not bigger than 100.\n", x ); 
  return 0; 
}
