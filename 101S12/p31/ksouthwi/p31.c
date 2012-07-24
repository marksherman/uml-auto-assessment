/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 31: Inner Product of Two Vectors */
/*                                          */
/* Approximate completion time: 25  minutes */
/********************************************/

#include <stdio.h>

float inner( float u[] , float v[] , int size );

int main( int argc , char* argv [] ) {

  int i , j , size = 8 ;
  float u[8] , v[8] ;

  for( j = 1 ; j < 3 ; j++ ){ /*reiterates for each vector*/

    printf( "Input %d numbers for vector %d \n" , size , j );

    for( i = 0 ; i < size ; i ++ ){
      /*reiterates for each element in the jth vector*/

      if(  j == 1  ) /*only fills in vector 1 which is u*/

	scanf( "%f" , &u[ i ] );

      else /*fills in the 2nd vector, v*/

	scanf( "%f" , &v[ i ] );

    }

  }

  printf( "the inner product is %f \n" , inner ( u , v , size ) );

  return 0;

}

float inner( float u[] , float v[] , int size ){

  int i;
  float a = 0;

  for( i = 0 ; i < size ; i++ ) /*reiterates for each element of the vectors*/

    a = ( u[i] * v[i] ) + a ;

  return a;

}
