/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 37: Digit Sum (again)               */
/*                                            */
/*Approximate completeion time: 20 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int digitsum( int num );

int main(int argc, char* argv[]){

  FILE* fin;

  int x, y;

  fin = fopen( argv[1], "r" );

  while( fscanf( fin, "%d", &y ) != EOF ){ 

  x = digitsum( y );

  printf( "the sum of the individual integeres are: %d\n", x ); 
  }

  fclose( fin );

  return 0;
}

int digitsum( int num ){

  int z, g=0;

  while( num > 0 ){

    z = num%10;

    g = g+z;

    num = num/10;

  }

  return g;

}
