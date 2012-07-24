/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 19 : Argv                       */
/*                                         */
/* Approximate completion time: 60 minutes */
/*******************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int x;
  for ( x = 0; x < argc; x++ )
    printf( "%s\n", argv[x] );
  return 0;  
}
