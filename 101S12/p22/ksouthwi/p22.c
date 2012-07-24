/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 22: Sum of a Bunch                 */
/*                                            */
/* Approximate completion time: 10  minutes   */
/**********************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  int x , i ;

  FILE *fin;

  fin = fopen( "testdata22" , "r" );

  for( i = 0 ; (fscanf ( fin ,  "%d" , &x )) != EOF ; i = ( i + x ) );

  printf("%d \n" , i );

  fclose( fin );

  return 0;

}
