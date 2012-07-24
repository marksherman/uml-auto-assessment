/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 19: Argv                                 */
/*                                                  */
/* Approximate completion time: 5  minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int i;
  char* x;

  printf("\n");
  
  for( i = 0; i < argc; i++ ){
    x = argv[i];
    printf("%s\n", argv[i]);
  }
  
  return 0;
}
