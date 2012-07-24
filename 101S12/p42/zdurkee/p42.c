/**************************************************************/
/*  Programmer: Zachary Durkee                                */
/*                                                            */
/*  Program 42: Malloc up Space for a Two-Dimensional Array   */
/*                                                            */
/*  Approximate completion time: 30 minutes                   */
/**************************************************************/

#include <stdio.h>

#include <stdlib.h>

int main( int argc, char *argv[]){

  int r, c, i, j, num, reqrow, reqcol, rowsum = 0, colsum = 0, sum = 0;

  int *values;

  printf( "Enter the number rows and columns:\n" );

  scanf( "%d %d", &r, &c );

  values = (int *) malloc( r * c * sizeof( int ) );

  for ( i = 0; i < r; i++ ){  /* allocates the row*/

    for ( j = 0; j < c; j++ ){  /* allocates the columns in a row*/

      printf( "Enter number into values[%d][%d]:\n", i, j );

      scanf( "%d", &num );

      values[ i * c + j ] = num;

    }

  }

  printf( "Which row would you liked to be summed:\n" );

  scanf( "%d", &reqrow );    /* Requests the row number*/

  for ( j = 0; j < c; j++ )  /*  Adds up the columns in the row */

    rowsum+= values[ reqrow * c + j ];
    
  printf( "%d\n", rowsum );

  printf( "Which column would you liked to be summed:\n" );

  scanf( "%d", &reqcol );   /* Requests the column number */

  for ( i = 0; i < r; i++ )    /* Adds up the rows in the column*/

    colsum += values[ i * c + reqcol ];

  printf( "%d\n", colsum );

  for ( i = 0; i < r; i++ )

    for( j = 0; j < c; j++ )

      sum += values[ i * c + j ];

  printf( "The sum of the arrary is: \n%d\n", sum );

  free( values );

  return 0;

}
