/************************************************/
/* Programmer: Kyle White                       */
/* Program  24: Find the Average                */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int x = 0;
  float sum=0;
  float avg = 0;
  FILE *fin;

  fin = fopen ( "testdata24", "r" );

  putchar ('\n');

  while ( fscanf (fin, "%d", &x) != EOF ) {

    sum = sum + x;

    avg = sum/4;

  }

  printf ("%f", avg);

  fclose (fin);

  putchar ('\n');
  putchar ('\n');

  return 0;

}
