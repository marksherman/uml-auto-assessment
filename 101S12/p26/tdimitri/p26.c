/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 26: One Dimensional Array             */
/* Approximate completion time: 55 mins          */
/*************************************************/

#include<stdio.h>

int main(int argc, char* argv[]) {
  int i[15], x;
  x = 0;
  FILE *fin;
  fin = fopen("testdata26", "r");

  while( x <= 15){
    fscanf(fin, "%d", &i[x]);
    x++;
  }
  for( x = 14; x >= 0; x--)
    printf("%d ", i[x]);
  fclose(fin);
  putchar('\n');

  return 0;
}
