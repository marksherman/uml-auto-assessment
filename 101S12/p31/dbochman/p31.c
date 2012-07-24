/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 31:   Inner Product of Two Vectors           */
/* Time:         20 minutes                             */
/********************************************************/

#include <stdio.h>

float inner ( float u[], float v[], int size ) {

  int i ;

  for ( i = 0 ; i < 8 ; i++ ) size += u[i] * v[i] ;

  return size ;
}

int main ( int argc, char *argv[] ) {

  int i ;
 
  float u[8], v[8] ;

  printf ( "\nPlease enter 8 values for vector u: \n" ) ;

  for ( i = 0 ; i < 8 ; i++ ) scanf ( "%f", &u[i] ) ;

  printf ( "\nPlease enter 8 values for vector v: \n" ) ;

  for ( i = 0 ; i < 8 ; i++ ) scanf ( "%f", &v[i] ) ; 

  printf ( "\nThe inner product of the two vectors is %f.\n\n", inner ( u, v, 0	) ) ;
  return 0 ;
}
