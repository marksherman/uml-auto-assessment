/***************************************************/
/* Programmer: Joe LaMarca                         */
/* Program: p12 sqrt function                      */
/* Approximate time of completion: 10 min          */
/***************************************************/

#include <stdio.h>
#include <math.h>

int main (int arc, char* argv[]){

  int x;

  printf("Enter a value:");
  scanf("%d",&x);
  
  x=sqrt(x);
 
  printf("%d\n",x);

  return 0;
}
