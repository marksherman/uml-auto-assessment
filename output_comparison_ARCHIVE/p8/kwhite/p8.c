/************************************************/
/* Programmer: Kyle White                       */
/* Program 8: One Horizantal Line of Asterisks  */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main ()

{

  FILE* fin;
  int x;
  int a;

  fin = fopen ("testdata8", "r");

  fscanf ( fin, "%d", &x); 

  for (a = 0; a < x; a ++ ){

    printf("*");

}

  putchar ( '\n' );

  fclose (fin);

  return 0;

}
