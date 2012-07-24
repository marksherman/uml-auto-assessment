/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 22: Sum of a Bunch                    */
/* Approximate completion time: 20 mins          */
/*************************************************/

#include <stdio.h>

int main( int argc, char *argv[]){
  FILE *fin;
  int x, sum;
  sum = 0;
  fin = fopen ("testdata22", "r");
  while( fscanf( fin, "%d", &x) != EOF)
    sum = x + sum;
  printf( "The sum of the numbers is: %d\n", sum);
  fclose(fin);

  return 0;
}
