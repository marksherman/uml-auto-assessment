/****************************************/
/* Programmer: Rachel Driscoll          */
/*                                      */
/* Program: Inner Product of Two Vectors*/
/*                                      */
/* Approx Completion Time: 30 min       */
/****************************************/

#include <stdio.h>
#include <ctype.h>
#include <math.h>

float inner( float a[], float b[], int length );


int length = 8;


int main( int argc, char *argv[]){

	int i;
	float x;
  float u[8];
  float v[8];
  
  printf( "Enter in 8 digits from 0 to 7 with a space between them in any order:\n");

  for( i = 0; i < 8; i++ ){
    scanf( "%f", &u[i] );
  } 

  printf( "Enter in 8 digits from 0 to 7 with a space between them in any order:\n");

  for( i = 0; i < 8; i++ ){
    scanf( "%f", &v[i] );
  }

  x = inner( u ,v, length );
  
  printf( "The value is:%f\n", x );

  return 0;
}

float inner( float a[], float b[], int length){
	float sum = 0;
	int i;

	for(i = 0; i < length; i++){
		sum += a[i] * b[i];
	}
  
	return sum;
}
