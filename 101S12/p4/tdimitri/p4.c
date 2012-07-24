/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 1: The fscanf Function                */
/* Approximate completion time: 25 mins          */
/*************************************************/
#include <stdio.h>
int main() {
  FILE *fin;
  int x;
  fin = fopen ("testdata4", "r");
  fscanf( fin,"%d", &x);
  fclose(fin);
  printf( "The value is: %d\n", x);

  return 0;
}
