/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Find the Average                                             */
/*                                                                       */
/* Approximate completion time: 25 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc, char *argv[]) {

  int x, y = 0;
  FILE *fin;

  fin = fopen("testdata24", "r");
  while (fscanf(fin, "%d", &x)!=EOF){
    y += x;
  }

  printf("The average is %lf\n", y/4.0);
  return 0;
}
