/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 26: One Dimensional Array           */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  FILE* fin;

  int x[15];

  int i;

  fin = fopen( "testdata26", "r" );

  for( i=0; fscanf( fin, "%d", &x[i] ) != EOF; i++ );
       
  for( i=14; i>=0; i-- )

    printf( "%d ", x[i] );

  putchar( '\n' );

  return 0;
}
