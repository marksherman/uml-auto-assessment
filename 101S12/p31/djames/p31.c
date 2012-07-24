/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 31: Inner Product of Two Vectors    */
/*                                            */
/*Approximate completeion time:  minutes      */
/**********************************************/

#include <stdio.h>

float inner( float u[], float v[], int size );

int main(int argc, char* argv[]){  

  int x=0;

  float y=0;

  float arr1[8];

  float arr2[8];

  printf( "enter 8 numbers" );

  for( x=0; x<8; x++ )

    scanf( "%f", &arr1[x] );

  printf( "enter another 8 numbers" );

  for( x=0; x<8; x++ )

    scanf( "%f", &arr2[x] );

  y = inner( arr1, arr2, 8 );

  printf( "the inner product of the two arrays is: %f\n", y );

  return 0;
}

float inner( float u[], float v[], int size ){

  int num;

  float tmp=0, var=0;

  for( num=0; num<size; num++ ){ 

    tmp = u[num] * v[num];

    var = var + tmp;
  }

  return var;
}
