/************************************************/
/* Programmer: Kyle White                       */
/* Program  15: Solid Box of Asterisksn         */
/* Approximate completion time:                 */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int a,x,y,b
;

  printf ( "Please Enter a Width:" );

  scanf ("%d", &x);

  printf ( "Please Enter a Height:");

  scanf ("%d", &y);

  for (a=0; a<x; a++){

    for (b=0; b<y; b++){

      printf( "*" );

    }

    putchar ('\n');

  }

  putchar ('\n');

  return 0;

}
