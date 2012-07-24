/*******************************************************************************/
/*  Programmer: Zachary Durkee                                                 */
/*                                                                             */
/*  Program 41: Malloc up Space for a 1-Dimensional Arrary of n integers       */
/*                                                                             */
/*  Approximate completion time: 30 minutes                                    */
/*******************************************************************************/

#include <stdio.h>

#include <stdlib.h>

int main( int argc, char *argv[] ){

  int n, i, sum = 0;

  int *array;

  printf( "Enter the length of the array\n" );

  scanf( "%d", &n );

  array = (int *) malloc( n * sizeof(int) );

  printf( "Enter the values into the array\n" );

  for( i=0; i<n; i++ )

    scanf( "%d", &array[i] );

  for( i=0; i<n; i++ )

    sum += array[i];

  printf( "The sum of the array is:\n%d\n", sum );

  free( array );

  return 0;

}
