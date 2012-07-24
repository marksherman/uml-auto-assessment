/*************************************************/
/* Programmer: Joanna Sutton                     */
/*                                               */
/* Assignment: Simulating Call by Reference      */
/*                                               */
/* Approximate Completion Time: 10 minutes       */
/*************************************************/

#include <stdio.h>
#include <stdlib.h>

void swap (int *a, int *b);

int main(int argc, char *argv[]){
  int x;
  int y;
  
  x=atoi(argv[1]);
  y=atoi(argv[2]);

  swap (&x, &y);

  return 0;

}

void swap (int *a, int *b){
  int temp;

  temp=*a;
  *a=*b;
  *b=temp;

  return;

}
