/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 41: Malloc up Space for a 1-Dimensional  */
/*             Array of n intergers                 */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv[] ){

  int x = 0;
  int i;
  int *array;
  int sum = 0;

  printf("\nEnter a number for the size of the array.\n");
  scanf("%d", &x);

  array = ( int * )malloc( x * sizeof(int) ); /* Array is the size of the num
						 scanned * sizeofint.        */
  printf("\nEnter numbers to fill the array\n");
  
  for(i = 0; i < x; i++){  /* Scans for a new number until array is full. */
    scanf("%d", &array[i]);
  }

  for( i = 0; i < x; i++){ /* Adds numbers together in array. */
    sum += array[i];
  }

  free(array); /* Frees the space allocated by malloc. */
  printf("\nThe sum of the numbers in the array is: %d\n", sum);

  return 0;
}
