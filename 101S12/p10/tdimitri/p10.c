/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 10: Sum of Twenty                     */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include <stdio.h>
int main() {
  FILE *fin;
  int x,i, sum;
  sum=0;
  fin = fopen ("testdata10", "r");
  for(i=0; i<20; i++){
    fscanf( fin, "%d", &x);
    sum= x+sum;
  }
  printf( "%d", sum );
  fclose(fin);
  putchar('\n');
  return 0;
}
