/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 23: fgetc and toupper                    */
/*                                                  */
/* Approximate completion time: 30 minutes          */
/* (getting ride of the "implicit declaration of    */
/* toupper took me a long time.)                    */
/****************************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char* argv[] ){

  FILE* fin;
  int x = 0;
  int y = 0;

  fin = fopen("testdata23", "r");

  while( (x = fgetc( fin )) != EOF ){
    fgetc( fin );
    y = toupper(x);
    putchar(y);
    printf("\n");
  }

  return 0;
}
