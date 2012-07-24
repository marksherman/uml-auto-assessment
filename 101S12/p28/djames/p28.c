/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 28: Digit Sum                       */
/*                                            */
/*Approximate completeion time: 45 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int digitsum( FILE* a );

int main(int argc, char* argv[]){

  FILE* fin;

  int x;

  fin = fopen( argv[1], "r" );

  x = digitsum( fin );

  printf( "the sum of the individual integeres are: %d\n", x ); 

  fclose( fin );

  return 0;
}

int digitsum( FILE* a ){

  int y,z,g=0;

  fscanf( a, "%d", &y );

  while( y > 0 ){

    z = y%10;

    g = g+z;

    y = y/10;

  }

  return g;

}
