/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 16: Count Characters                     */
/*                                                  */
/* Approximate completion time: 30 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  char nextchar;
  int total;

  printf( "Enter characters.\n" );
  nextchar = getchar();
  
  total = 0;
  while( nextchar != EOF ){
    total++;
    nextchar = getchar();
  }
  printf("\nYou entered %d characters.\n", total);

 return 0;
}
