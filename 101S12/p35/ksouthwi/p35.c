/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 35: Passing a Two Dimensional Array */
/*                                             */
/* Approximate completion time: 25  minutes    */
/***********************************************/

#include <stdio.h>

int sum( int u[][3] , int size );

int main( int argc , char* argv [] ) {

  int i , j , matrix[3][3] , size = 3 ;

  printf ( "Input 9 integers \n" );
  
  for( j = 0 ; j < 3 ; j++ ){ /*reiterates for each row*/

    for( i = 0 ; i < size ; i ++ ){
      /*reiterates for each element in the jth row*/

      scanf( "%d" , &matrix[i][j] );

    }

  }

  printf( "the sum of the elements is %d \n" , sum( matrix , size ) );

  return 0;

}

int sum( int u[][3] , int size ){

  int i , j , sum = 0;

  for ( j = 0 ; j < 3 ; j++ ) /*reiterated for each row*/

    for( i = 0 ; i < size ; i++ ) /*reiterates for each element in each  row*/

      sum += u[i][j] ;

  return sum;

}
