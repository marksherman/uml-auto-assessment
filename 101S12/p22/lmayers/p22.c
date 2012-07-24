/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Sum of a Bunch                                               */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc, char *argv[]) {

  int x, y = 0;
  FILE *fin;

  fin = fopen("testdata22", "r");
  while (fscanf(fin, "%d", &x)!=EOF){
    y += x;
  }

  printf("The average is %d\n", y);
  return 0;
}
