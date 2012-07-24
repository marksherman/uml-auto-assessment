/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 8: One Horizontal Line of Asterisks   */
/* Approximate completion time: 95 mins          */
/*************************************************/
#include <stdio.h>
int main() {
  FILE *fin;
  int x, i;
  fin = fopen ("testdata8", "r");
  fscanf( fin,"%d", &x);
  fclose(fin);
  for(i=0; i<x; i++){
  printf( "*");
  }
  putchar('\n');
  return 0;
}
