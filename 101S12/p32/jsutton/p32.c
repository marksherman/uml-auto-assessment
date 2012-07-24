/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: Non-recursive Factorial     */
/*                                         */
/* Approximate Completion Time: 20 minutes */
/*******************************************/

#include <stdio.h>

int fact(int a);

int main (int argc, char *argv[]){
  int x;
  int y;

  printf("Please enter an integer.\n");
  scanf("%d",&x);

  y=fact(x);
  
  printf("The factorial of that number is %d.\n", y);

  return 0;

}

int fact(int a){
  int i;
  int k=1;

  if((i=2)<a){
    for (i=2;i<a+1;i++)
    k=i*k;
  }

  if((i=2)>a)
    k=1;

  return k;

}
      
    
