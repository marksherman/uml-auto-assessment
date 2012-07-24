/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 42: Malloc up Space for a Two-Dimensional*/
/*             Array                                */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv[] ){

  int r = 0;
  int c = 0;
  int i;
  int j;
  int* array;
  int x = 0;
  int sum = 0;

  printf("\nEnter the number of rows in the array\n");
  scanf("%d", &r);

  printf("\nEnter the number of columns in the array\n");
  scanf("%d", &c);

  printf("\nEnter %d integers to populate the array\n", r*c);

  array = ( int* )malloc( r * c * sizeof(int) );

  for( i = 0; i < c; i++ ){
    for( j = 0; j < r; j++ ){
      scanf("%d", &array[i*c+j]);
    }
  }		   

  printf("\nWhich row, from 0 to %d, would you like to sum?\n", r-1);
  scanf("%d", &x);

  for( i = 0; i < r; i++ ){
    sum += array[x*c+i];
  }

  printf("\nThe sum of row %d, is %d\n", x, sum);

  printf("\nWhich column, from 0 to %d, would you like to sum?\n", c-1);
  scanf("%d", &x);

  sum = 0;

  for( i = 0; i < c; i++ ){
    sum += array[x+i*c];
  }
  printf("\nThe sum of column %d, is %d\n", x, sum);

  sum = 0;

  for( i = 0; i < r*c; i++ ){
    sum += array[i];
  }
  printf("\nThe sum of the array is %d\n", sum);
  
  free(array);
  return 0;

}
