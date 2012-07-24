/********************************************/
/* Programmer: Kyle White                   */
/* Program 4: fscanf                        */
/* Approximate Completion Time: 15 minutes  */
/*                                          */
/********************************************/





#include <stdio.h>

int main ()

{

  FILE* fin;
  int x;

  fin = fopen( "testdata4.c", "r");

  fscanf( fin, "%d", &x);

  printf( "\n%d\n\n", x);

  fclose( fin );

  return 0;

}
