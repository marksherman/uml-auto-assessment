/************************************************/
/* Programmer: Kyle White                       */
/* Program  22: Sum of a Bunch                  */
/* Approximate completion time: 5 Minutes       */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int x=0;
  int sum=0;
  FILE *fin;

  fin = fopen( "testdata22", "r");

  while ( fscanf(fin, "%d", &x) != EOF){

    sum = sum + x;

  }

  printf ("\nThe sum is: %d\n", sum);

  putchar ('\n');

  fclose (fin);

  return 0;

}
