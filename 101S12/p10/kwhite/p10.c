/************************************************/
/* Programmer: Kyle White                       */
/* Program 10: Sum of Twenty                    */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  FILE* fin;
  int x;
  int a;
  int sum;

  fin = fopen ( "testdata10", "r" );//ta changed testdata10.c to testdata

  sum = 0;

  putchar ('\n');

  for (a=0; a<20; a++){

    fscanf ( fin, "%d", &x);

    sum = sum + x;

  }

  printf( "%d", sum );

  fclose ( fin );

  putchar ('\n');
  putchar ('\n');

  return 0;

}
