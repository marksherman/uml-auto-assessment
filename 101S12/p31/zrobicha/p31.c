/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Inner Product of Two Vectors                                 */
/*                                                                           */
/* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/

#include <stdio.h>
#include <string.h>

float inner( float u[] , float v[] , int size ) ;

int main( int argc , char* argv[] ) {
  
  float u[8] , v[8] , sum ;
  int size = 8 , i ;
  
  printf( "Enter the first vector\n" ) ;
  for( i = 0 ; i < 8 ; i++ ) 
    scanf( "%f" , &u[i] ) ;
  printf( "Enter the second vector\n" ) ;
  for( i = 0 ; i < 8 ; i++ ) 
    scanf( "%f" , &v[i] ) ;
  sum = inner( u , v , size ) ;
  printf( "The inner sum of the two vectors is %f\n" , sum )  ;
  return 0 ; 
}

float inner( float u[] , float v[] , int size ) {
  
  int i , insum = 0 ;

  for( i = 0 ; i < size ; i++ ) 
    insum += u[i] * v[i] ;
  return insum ;
}
