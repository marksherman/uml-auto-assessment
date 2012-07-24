/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Malloc up Space for 2D Array     */
/*                                            */
/* Approximate completion time: 30 minutes    */
/**********************************************/

#include<stdio.h>
#include<stdlib.h>

int main( int argc, char *argv[] ) {

  int rowsum = 0, colsum = 0, i, j, r, c, inputrow, inputcol;
  int* number;

  printf( "Please enter the row and column: " );
  scanf( "%d %d", &r, &c );

  number = malloc( r * c * sizeof( int ) );

  printf( "Please enter the numbers: " );

  for( i = 0; i < r; i++ ) {
    for( j = 0; j < c; j++ ) {
      scanf( "%d", &number[ i * c + j ]);
    }
  }

  printf( "Which row do you want to sum up? " );
  scanf( "%d", &inputrow );

  for( i = 0; i < c; i++ ) {
    rowsum = rowsum + number[ ( inputrow - 1 ) * c + i ];
  }
  
  printf( "The sum of the row is %d.\n", rowsum );

  printf( "Which column do you want to sum up? " );
  scanf( "%d", &inputcol );

  for( i = 0; i < r; i++ ) {
    colsum = colsum + number[ ( inputcol - 1 ) + ( c * i ) ];
  }
  
  printf( "The sum of the column is %d.\n", colsum );

  free( number );

  return 0;
}
