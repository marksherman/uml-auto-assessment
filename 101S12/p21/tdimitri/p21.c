/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 21: scanf returns what?               */
/* Approximate completion time: 25 mins          */
/*************************************************/

#include <stdio.h>

int main( int argc, char *argv[]){
  FILE *fin;
  int x;
  fin = fopen ("testdata21", "r");
  while( fscanf( fin, "%d", &x) != EOF){
    printf( "%d ", x);
      }
  putchar('\n');
  fclose(fin);

  return 0;
}
