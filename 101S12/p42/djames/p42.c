/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 42: Malloc a 2-D Array              */
/*                                            */
/*Approximate completeion time: 2 hours       */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){  

  int r, c, j, i, row, column;

  int sum_row=0;
  int sum_column=0;
  int sum_all=0;


  int* int_ptr;

  printf( "Enter the number of rows for the array\n" );

  scanf( "%d", &r );

  printf( "Enter the number of columns for the array\n" );

  scanf( "%d", &c );

  int_ptr = (int*) malloc( r * c * sizeof (int) );

  for( j=0; j<r; j++ ){

    for( i=0; i<c; i++ ){
      
      printf( "Enter a value for the array\n" );
      
      scanf( "%d", &int_ptr[ (j*r+i) ]);
    }
  }
  printf( "Choose a row you wish to find the sum of from 0 to %d\n", (r-1) );

  scanf( "%d", &row );

  for( i=0; i<r; i++ )

    sum_row = sum_row + int_ptr[ ( row*r + i ) ];

  printf( "The sum of the row is %d\n", sum_row );

  printf( "Choose a column you wish to find the sum of for 0 to %d\n", (c-1) );

  scanf( "%d", &column );

  for( j=0; j<c; j++ )

    sum_column = sum_column + int_ptr[ ( j*r + column ) ];

  printf( "The sum of the column is %d\n", sum_column );

  for( j=0; j<r; j++ ){

    for( i=0; i<c; i++ ){

      sum_all = sum_all + int_ptr[ (j*r+i) ];
    }
  }
  printf( "The sum of the entire array is %d\n", sum_all );

  free( int_ptr );

  return 0;
}
