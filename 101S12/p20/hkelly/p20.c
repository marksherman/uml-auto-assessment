/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 20: Reverse the Command Line             */
/*                                                  */
/* Approximate completion time: 30 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int i;
  char* x;
  
  for(i = argc-1;  i >= 0; i-- ){
    x = argv[i];
    printf("\n%s", x);
    }
  printf("\n");

  return 0;
}
