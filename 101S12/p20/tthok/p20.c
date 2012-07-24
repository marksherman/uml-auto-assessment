/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 20 : Reverse the Command Line   */
/*                                         */
/* Approximate completion time:180 minutes */
/*******************************************/

#include <stdio.h>

int main( int argc, char *argv[])
{
  while ( argc-- > 0 )
    printf( "%s\n", argv[argc] );
  return 0;  
}
