/************************************************/
/* Programmer: Kyle White                       */
/* Program  20: Reverse the command Line        */
/* Approximate completion time: 15 Minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int i;

  for (i=1 ; i<=argc ; i++) {

    printf(argv[argc-i]);

    putchar('\n');

  }

  return 0;

}
