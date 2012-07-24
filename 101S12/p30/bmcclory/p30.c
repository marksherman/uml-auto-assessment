/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #30: Simulating Call By Reference             */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int swap(int *a, int *b);

int main(int argc, char *argv[]){

  int x, y;

  printf("Type two integer values: ");
  scanf("%d %d", &x, &y);

  swap(&x, &y);

  printf("%d%c%d\n", x, 32, y);

  return 0;

}

int swap(int *a, int *b){

  int temp;

  temp = *a;
  *a = *b;
  *b = temp;

  return 0;
}
