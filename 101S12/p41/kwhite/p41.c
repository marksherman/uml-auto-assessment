/************************************************/
/* Programmer: Kyle White                       */
/* Program  41: Malloc Space for a 1-D array    */
/* Approximate completion time: 15 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <stdlib.h>

int array_sum ( int* array, int size );
int main (int argc, char* argv [])

{

  int i=0,number=0,sum=0;
  int n=0;
  int* arr_ptr;

  printf ( "\nEnter the size of the array: " );

  scanf ( "%d", &n );

  arr_ptr = ( int* ) malloc ( n * sizeof ( int ) );

  printf ( "Enter %d elements to be summed: ", n );

    for ( i=0 ; i<n ; i++ ){

      scanf ( "%d", &number );

      arr_ptr[i] = number;

    }

  sum = array_sum ( arr_ptr, n );

  printf ( "The sum of the elements of the array is: %d\n\n", sum );

  free ( arr_ptr );

  return 0;

}

int array_sum ( int* array, int size )

{

  int sum=0,i=0;

  for ( i=0 ; i<size ; i++ ){

    sum += array[i];

  }

  return sum;

}
