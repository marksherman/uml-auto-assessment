/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 6: Equal to zero?                       */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int step_one( int array[][15] , int N , int n , int i , int j , int stop ) ;

int prime( int n ) ;


int main( int argc, char *argv[] ) { 

  int array[15][15] ;
  int n = 0 ;
  int intial_value = 0 ;
  int i = 0 ;
  int j = 0 ;
  int check = 0 ;

  /* Promting user for dimensions of square */

  printf( "Enter the size of your square box\n" ) ;
  scanf( "%d" , &n ) ;
  
  /* Promting user for intial value of middle */

  printf( "Enter the intial value of the center of the box\n" ) ;
  scanf( "%d" , &intial_value ) ;

  /* Attempting to store numbers into array[15][15] with function step_one */

  array[15][15] = step_one( array , n-1 , n-1 , n-1 , n-1 , 0 ) ;
  
  /* Using nested for loop to print out the entire array, using prime function to check prime */

  for ( i = 0 ; i < n ; i++ ){
    for ( j = 0 ; j < n ; j++ ){
      
      check = array[i][j]+intial_value ;
      
      if ( prime( check ) == 0 )
	printf( "%d " , check ) ;

      if ( prime( check ) == 1 )
	printf( "*** " ) ;

      if ( j == n )
	printf( "\n" ) ;
      
      
    }
  }

  return 0 ;
  
}

int step_one( int array[][15] , int N , int n , int i , int j , int stop ) {

  /* This function attempts to start from the outside of the square box, with the highest number, then going around the edges based on n-1 value. It then attempts to repeat the process with recursion n-1-1 etc. */

  int mid = 0 ;
  int start = 0 ;
  
  start = (n+2) * n ;

  /* Recursive base case - fills in center with 0 */

  if ( n < 3 ) {
    mid = N - 1 ;
    mid = mid / 2 ;
    array[mid][mid] = 0 ;
    return array[15][15] ;
  }

  /* for loops filling in the edges of the square bases on n-1 */

  for ( i = n ; i > stop ; i++ ) {
    array[i][j] = start ;
    start-- ;
  } 

  i = stop ;

  for ( j = n ; j > stop ; j-- ) {
    array[i][j] = start ;
    start-- ; 
  }

  j = stop ; 

  for ( i = stop ; i < n ; i++ ) {
    array[i][j] = start ;
    start-- ;
  }

  i = n ;
  
  for ( j = stop ; j < n-1 ; j++ ) {
    array[i][j] = start ;
    start-- ;
  }

  /* Returning to step_one */

  return step_one( array , N , n-1 , n-1 , n-1 , stop+1 ) ;  

  }

int prime( int n ) {
  
  /* Check prime function */
  
  int i ;

  for ( i = 2 ; i <= n-1 ; i++ ) {
    
    if ( n % i == 0 )
      return 0 ;
  }

  if ( i == n ) 
    return 1 ;

  else 
    return 0 ;
}
