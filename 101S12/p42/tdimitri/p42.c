/***********************************************************/
/* Programmer: Theodore Dimitriou                          */
/* Program 42: Malloc up Space for a Two-Dimensional Array */
/* Approximate completion time: 4:30 hours                 */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>

int rowsum ( int x, int lengthofROW, int* ptr );
int columnsum ( int y, int lenthofROW, int* ptr );
int totalsum ( int row, int column, int* ptr );

int main ( int argc, char* argv[] ) {
  
  int i = 0, j = 0, r = 0, c = 0, row = 0, column = 0;
  int * ptr;
  
  printf( "\nEnter the dimensions of the array, r(ows) x c(olumns): " );
  scanf( "%d%d", &r, &c );
  ptr = malloc ( r * c * sizeof( int ) );
  printf( "\nEnter data to fill up the array:\n" );
  
  for( i = 0; i < r; i++ ){
    for( j = 0; j < c; j++)
      scanf( "%d", &ptr[ i * c + j ] );
  }

  printf( "\nWhich row would you like summed?\n" );
  scanf( "%d", &row );
  printf( "\n%d", rowsum ( row, c, ptr ) );

  printf( "\nWhich column would you like summed?\n" );
  scanf( "%d", &column );
  printf( "\n%d", columnsum ( column, c, ptr ) );

  printf( "\nThe sum of the entire array is: %d\n\n" , totalsum( r, c, ptr ) );
  
  free( ptr );
  
  return 0;
}

int rowsum ( int x, int lengthofROW, int* ptr )
{
  int i = 0, j = 0, sum = 0;
  
  i = x;
  for( j = 0; j < lengthofROW; j++ )
    sum += ptr[ i * lengthofROW + j ];
  
  return sum;
}

int columnsum ( int y, int lengthofROW, int* ptr )
{
  int i = 0, j = 0, sum = 0;

  j = y;
  for( i = 0; i < lengthofROW; i++ )
    sum += ptr[ i * lengthofROW + j ];
  
  return sum;
}

int totalsum ( int row, int column, int* ptr )
{
  int i = 0, j = 0, sum = 0;
  
  for( i = 0; i < row; i++ )
    for( j = 0; j < column; j++ )
      sum += ptr[ i * column + j ];
  
  return sum;
}
