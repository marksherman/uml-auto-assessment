/************************************************/
/* Programmer: Kyle White                       */
/* Program 9: Using a for Loop                  */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  FILE* fin;
  int x;
  int a;

  fin = fopen ( "testdata9", "r" );

  putchar ('\n');

  for (a=0; a<5 ; a++){

    fscanf ( fin, "%d", &x );

    printf ("%d ", x);

  }

  putchar ('\n');
  putchar ('\n');

  fclose ( fin );

  return 0;

}
