/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 30: Simulating Call by Reference                       */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>

int swap( int *a, int *b );
int main( int argc, char* argv [] ){
  
  int x;
  int y; 
  
  printf( "Enter an integer value for x: " );
  scanf( "%d", &x );
  printf( "Enter an integer value for y: " );
  scanf( "%d", &y );
  
  swap( &x, &y );
  printf("The integer now contained in x is %d\n", x);
  printf("The integer now contained in y is %d\n", y);
  return 0;    
}

int swap( int *a, int *b ){   
  
  int temp;
   
  temp = *a;
  *a = *b;
  *b = temp;
  
  return 0;  
}
 

