/************************************************/
/* Programmer: Kyle White                       */
/* Program  23: fgetc and toupper               */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <ctype.h>

int main (int argc, char* argv [])

{

  int x,y=0;
  FILE *fin;
  int toupper();

  fin = fopen ( "testdata23" , "r" );

  putchar ('\n');

  while ( (x=fgetc (fin)) != EOF ){

    y = toupper ( x );

    printf ("%c", y);

  }

  putchar ('\n');

  return 0;

}
