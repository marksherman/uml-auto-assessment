/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 22: sum of a bunch                  */
/*                                            */
/*Approximate completeion time: 7  minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  FILE* fin;

  int x=0;

  int z=0;

  fin = fopen( "testdata22", "r" );

  while( fscanf( fin, "%d", &x ) != EOF ){

    z=z+x;
    
  }

  printf( "%d\n", z );

  return 0;
}
