/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 41: Malloc for 1D Array             */
/*                                            */
/*Approximate completeion time: 20 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){  

  int n;

  int x;

  int sum=0;

  int* int_ptr;

  printf( "Enter the number of integers planned to be entered\n" );

  scanf( "%d", &n );

  int_ptr = ( (int*) malloc( n * sizeof( int ) ));

  for( x=0; x<n; x++ ){

    printf( "Enter a number\n" );

    scanf( "%d", &int_ptr[x] );
  }

  for( x=0; x<n; x++ ){

    sum = sum + int_ptr[x];
  }

  printf( "the sum of the numbers is: %d\n", sum );

  free( int_ptr );

  return 0;
}
