/**************************/
/* Programmer: David Hoyt */
/* Program: Malloc 2-D    */
/* Time: 2hrs             */

#include <stdio.h>
#include <stdlib.h>

int row_sum( int* nums, int w, int s );

int col_sum( int* nums, int w, int h, int s );

int main(){

  int r, c, i, s,  arr_sum=0;

  int* nums;

  printf( "Enter number of rows:" );

  scanf( "%d", &r );

  printf( "Enter number of columns:" );

  scanf( "%d", &c );

  nums = (int*) malloc( r * c * sizeof(int));

  printf( "Enter numbers for the array:" );

  for( i=0; i<(r*c); i++ ){

    scanf( "%d", &nums[i] );

  }

  printf( "Which row would you liked summed?" );

  scanf( "%d", &s );

  printf( "Sum of row %d is: %d\n", s, row_sum( nums, c, s-1 ) );

  printf( "Which column would you liked summed?" );

  scanf( "%d", &s );

  printf( "Sum of column %d is: %d\n", s, col_sum( nums, c, r, s-1 ) );

  for( i=0; i<(r*c); i++){

    arr_sum += nums[i];

  }

  printf( "Sum of array: %d\n", arr_sum );

  return 0;

}

int row_sum( int* nums, int w, int s ){

  int i, sum=0;

  for( i=0; i< w; i++ ){

    sum += nums[ s * w + i ];

  }

  return sum;

}

int col_sum( int* nums, int w, int h, int s ){ 

  int i;

  int sum=0;

  for( i=0; i<h; i++ ){

    sum += nums[ s + (w*i) ];

  }

  return sum;

}




