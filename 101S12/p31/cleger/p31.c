/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Inner Product of Two Vectors      */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/


#include<stdio.h>

float inner( float u[], float v[], int size );

int main( int argc, char *argv[] ){
  
  
  float vector1[8];
  float vector2[8];
  int i;
  
  printf( "Enter 8 floating point numbers seperated by spaces:" );
  
  for( i = 0; i<8; i++ ) {
    scanf( "%f", &vector1[i] );
  }
  
  printf( "Enter 8 floating point numbers seperated by spaces:" );
  
  for( i = 0; i<8; i++ ) {
    scanf ( "%f" , &vector2[i] );
  }
  printf( "The inner product is:%f\n", inner(vector1, vector2, 8) );
  
  return 0; 
  
}

float inner( float u[], float v[], int size ) {
  
  int i;
  int product = 0;
  
  for( i = 0; i<size; i++ ) {
    product += u[i] * v[i] ;
  }
  
  return product;
}
