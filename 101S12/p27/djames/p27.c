/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 27: Reverse                         */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int x[10];

  int i;

  printf( "enter ten integer numbers" );

  for( i=0; i<10; i++ )

    scanf( "%d", &x[i] );

  printf( "The ten numbers you entered in reverse order are:\n" );

  for( i=9; i>=0; i-- ){

    printf( "%d ", x[i]);

  }

  putchar( '\n' );

  return 0;
}
