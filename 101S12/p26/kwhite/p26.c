/************************************************/
/* Programmer: Kyle White                       */
/* Program  26: One Dimensional Array           */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int x;
  int a=0;
  int b=0;
  int array[15];
  FILE *fin;

  fin = fopen ("testdata26", "r");

  for ( a=0; a<15; a++){

    fscanf(fin, "%d", &x);

    array[a]=x;

  }

  for(b=14;b>=0;b--){

    printf("%d ", array[b]);

  }

  putchar ('\n');

  return 0;

}
