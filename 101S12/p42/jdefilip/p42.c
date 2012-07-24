/*****************************************/ 
/* Author: James DeFilippo               */ 
/* Title: Malloc Up Space for 2-D Array  */ 
/* Approximate Time: 60 minutes          */ 
/*****************************************/ 

#include <stdio.h>
#include <stdlib.h>
int main ( int argc, char* argv[] ) 
{
  int* x; /* size of array */ 
  int i;
  int j; 
  int sum = 0; 
  int r;
  int c; 
  int c_user; 
  int r_user; 
  printf( "Hi! Please enter the row size of the 2-D array.\n" ); 
  scanf( "%d", &r ); 
  printf( "Please enter the column size of the 2-D array.\n" ); 
  scanf( "%d", &c ); 
  x = ( int* ) malloc ( r * c * sizeof(int) ); 
  printf( "Please enter the values of the array.\n" ); 
  for ( j = 0; j < c; j++) {
  for ( i = 0; i < r; i++) 
     scanf("%d", &x[i*c + j]); 
  }

  printf( "Which row would you like summed? " ); /* sum a specific row */ 
  scanf( "%d", &c_user );  
  for ( i = 0; i < r; i++ ) 
    sum = x[i*c + c_user] + sum; 
  printf( "The sum of row %d is %d.\n", c_user, sum); 
  sum = 0; 
  printf( "Which column would you liked summed? " ); /* sum a specific column */ 
  scanf( "%d", &r_user ); 
  for ( j = 0; j < c; j++ ) 
    sum = x[r_user*c + j] + sum; 
  printf( "The sum of column %d is %d.\n", r_user, sum); 
  sum = 0;
  for ( j = 0; j < c; j++ ) { 
    for ( i = 0; i < r; i++ ) 
      sum = x[ i*c + j ] + sum; 
  }

  printf ( "The sum of all the elements of the array is %d.\n", sum ); 
  free ( x ); 
  return 0; 
}
