/************************************************/
/* Programmer: Kyle White                       */
/* Program  21: scanf returns what?             */
/* Approximate completion time: 5 Minutes       */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int x=0;
  FILE *fin=0;

  fin = fopen ( "testdata21", "r" );

  while (fscanf(fin, "%d", &x) != EOF){

    printf( "%d ", x);

  }

  putchar ('\n');

  fclose (fin);

  return 0;

}
