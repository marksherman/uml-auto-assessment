/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 41: Malloc up Space for a 1-Dimensional Array  */
/*  Aproximate Completion time: 11 Minutes          */
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


  printf( "Enter the Size of your array\n" ) ;
  scanf( "%d" , &n ) ;


  array = ( int* ) malloc( sizeof( int ) * n ) ;

  for ( i = 0 ; i < n ; i++ ) {
    printf( "Enter a number to store into the array (size = %d )\n" , n ) ;
    scanf( "%d" , &num );
    array[i] = num ;
    
  }

  for ( i = 0 ; i < n ; i++ )
    sum += array[i] ;

  printf( "The sum of the array is = %d\n" , sum ) ;


  free( ( void* ) array ) ;

  return 0 ;

}
