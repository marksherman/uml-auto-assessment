/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 35: Passing a two Dimensional Array     */
/*  Aproximate Completion time: 25 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<stdlib.h>

int sum( int array[][3] , int len ) ;

int main( int argc, char *argv[] ) {

  int array[3][3] ;
  int i = 0 ; 
  int j = 0 ;
  int len = 3 ;

  /* Promting user */
  
  printf( "Enter 9 numbers for a 3 by 3 grid\n" ) ;
  
  /* Using two for loops to save 9 numbers into the 2D Array [3][3] */

  for ( i = 0 ; i < 3 ; i++ ) 
    for ( j = 0 ; j < 3 ; j++ )
      scanf( "%d" , &array[i][j] ) ;     	      
  
  /* Calling the sum function within printf and outputing the answer */

  printf( "The numbers you entered into the 3 by 3 grid at up to %d\n" , sum( array , len ) ) ;

  return 0 ;
}
       

int sum( int array[][3] , int len ) {

  int i = 0 ;
  int j = 0 ;
  int num = 0 ;

  /* Using += to store the sum of the 9 numbers in array[3][3] INTO num */

  for( i = 0 ; i < len ; i++ )
    for( j = 0 ; j < len ; j++ )
      num+= array[i][j] ;

  /* returning num into printf */

  return num ;

}
  
