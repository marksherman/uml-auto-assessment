/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : Inner product vectors */
/*Completion time: 10mins          */
/***********************************/

#include<stdio.h>


float inner( float u[] , float v[] , int size );

int main( int argc , char* argv[] ){

  float x[8];
  float y[8];
  float z;
  int i,j = 0;

  /*insert manual return then EOF after entering each sequence of floats*/
  printf( "Enter 8 floats for components of vector 1\n");
  for ( i = 0 ; scanf( "%f" , &x[i] ) != EOF ; i++ )
    ++j;

  printf("Enter 8 floats for components of vector 2\n");
  for ( i = 0 ; scanf( "%f" , &y[i] ) != EOF ; i++ );
 
  z = inner( x , y , j);
  printf( "The inner product is: %f\n" , z);
  
  return 0;
}

float inner( float u[] , float v[] , int size ){
  float sum = 0;
  int i;

  for ( i = 0 ; i < size ; i++ ){
    sum += u[i]*v[i];
  }
  return sum;
}
