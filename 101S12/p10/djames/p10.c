/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 10: Sum of Twenty                   */
/*                                            */
/*Approximate completeion time: 20 minutes    */
/**********************************************/

#include <stdio.h>
int main(){  

  int x, y, z;

  FILE* fin;

  fin = fopen( "testdata10", "r");

  z=0;

  for( x=0; x<20; x++){

    fscanf( fin, "%d", &y);

    z=(z+y);
  }

  printf( "%d", z);

  putchar( '\n' );

  fclose( fin );

  return 0;
}
