/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: Recursive Factorial         */
/*                                         */
/* Approximate Completion Time: 20 minutes */
/*******************************************/

#include <stdio.h>

int fact(int a);

int main (int argc, char *argv[]){
  int x;
  int y;

  printf("Please enter an integer.\n");
  scanf("%d", &x);

  y=fact(x);

  printf("The factorial of that number is: %d.\n", y);

  return 0;

}

int fact(int a){
  if(a==1||a==0)
    return 1;
  else
    return (a*fact(a-1));

}
