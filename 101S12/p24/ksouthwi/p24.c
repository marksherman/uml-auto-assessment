/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 24: Find the Average               */
/*                                            */
/* Approximate completion time: 10  minutes   */
/**********************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  int x , i ;
  float a = 0;
  FILE *fin;

  fin = fopen( "testdata24" , "r" );

  for( i = 0 ; i < 4 ; i ++ ){
  
    fscanf( fin ,  "%d" , &x );

    a = a + x ;

  }

  a = a / i ;

  printf("%f \n" , a );

  fclose( fin );

  return 0;

}
