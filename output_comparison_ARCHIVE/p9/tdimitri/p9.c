/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 9: Using a For Loop                   */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include <stdio.h>
int main() {
  FILE *fin;
  int x,i;
  fin = fopen ("testdata9", "r");
  for(i=0; i<5; i++){
    fscanf( fin, "%d", &x);
    printf( "%d ", x);
  }
  fclose(fin);
  putchar('\n');
  return 0;
}
