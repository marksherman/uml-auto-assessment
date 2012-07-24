/************************************************/
/* Programmer: Kyle White                       */
/* Program  19: Argv                            */
/* Approximate completion time: 5 Minutes       */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int i;

  for (i=0; i<argc; i++){

    printf(argv[i]);

    putchar('\n');

  }

  return 0;

}
