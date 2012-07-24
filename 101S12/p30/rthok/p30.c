/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 30: Simulating Call by Reference                              */
/*                                                                        */
/* Approximate Completion Time: 15 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
 
void swap( int *a, int *b );

int main( int argc, char *argv[] ) {
  
  int x, y ;

  printf("\nEnter a numbers for x:");
  scanf("%d", &x);

  printf("Enter a numbers for y:");
  scanf("%d", &y);
 
  printf("\nThe original value of x was %d and y was %d.\n", x,y);

  swap( &x , &y );

  printf("\nAfter being swap, the value of x is %d and y is %d.\n\n", x,y);
    
  return 0 ;
 
}
 
 void swap( int *a, int *b ) {
 
   int temp ;
 
   temp  = *a  ;
   *a  =  *b  ;
   *b  =  temp ;
 
   return ;
 
}
