/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 41: Malloc up Space for a 1-Dimensional Array  */
/*  Aproximate Completion time: 45 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<stdlib.h> 


int main( int argc, char *argv[] ) {

  int* array ; 
  int i = 0 ;
  int n = 0 ;
  int num = 0 ;
  int sum = 0 ;
  int row = 0 ;
  int col = 0 ;
  int rowQ = 0 ;
  int colQ = 0 ;


  printf( "Enter the size of your row \n" ) ;
  scanf( "%d" , &row ) ;

  printf( "Enter the size of your column\n" ) ;
  scanf( "%d" , &col ) ;


  n = row * col ;

  array = (int* ) malloc( sizeof( int ) * n ) ;

  for ( i = 0 ; i < n ; i++ ) {
    printf( "Enter a number to store into the array (size = %d )\n" , n ) ;
    scanf( "%d" , &num );
    array[i] = num ;
    
    }

  printf( "Which row would you liked summed?\n" ) ;
  scanf( "%d" , &rowQ ) ;
 

  i = row * rowQ + 1 ;
  while ( i <= col+rowQ*2 ) {
    sum+= array[i] ;
    i++ ;
  }
  
  if ( rowQ = 0 ) {
    sum = sum - array[i] ;
    sum = sum + array[0] ;
  }
  

  printf( "The sum of the array is = %d\n" , sum ) ;

  printf( "Which column would you liked summed?\n" ) ;
  scanf( "%d" , &colQ ) ;

  sum = 0 ; 
  i = colQ ;
  
  while ( i < row*col ) {
    sum+=array[i] ;
    i = i + col ;
  }

  printf( "The sum of that column is = %d\n" , sum ) ;

  sum = 0 ;

  for ( i = 0 ; i < n ; i++ ) 
    sum+=array[i] ;

  printf( "The sum of the entire array is = %d\n" , sum ) ;


  free( ( void* ) array ) ;

  return 0 ;

    
}
