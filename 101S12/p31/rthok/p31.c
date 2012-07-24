/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 31: Inner Product of Two Vector                                */
/*                                                                        */
/* Approximate Completion Time: 35 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

float inner( float A[], float B[], int size);

int main( int argc, char *argv[] ) {

  int i = 0, j = 0, x = 8 ;
  float A[8], B[8];


  printf("\nEnter %d numbers for A:", x);

  while(i < 8){
    scanf("%f", &A[i]);
    i++;
  }

  printf("Enter %d numbers for B:", x); 

  while(j < 8){
    scanf("%f", &B[j]);
    j++;
  }

  x = inner(A, B, x); 

  printf("\nThe inner product of A and B is %d.\n\n", x); 

  return 0 ;

}

float inner(float A[], float B[], int size){

  int z = 0, i;

  for(i = 0; i < size; i++){
    z = A[i] * B[i] + z;
  }

  return z;

}
