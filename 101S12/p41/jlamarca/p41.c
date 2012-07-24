/*******************************************************/
/* Programmer: Joe LaMarca                             */
/* Program: malloc up space                            */
/* Approximate time of completion:                     */
/*******************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int i, n, sum=0;
  int* var;

  scanf("%d",&n);

  var=(int*)malloc(n*sizeof(int));
  
  for(i=0; i<n; i++)
    scanf("%d",&*(var+i));

  for(i=0; i<n; i++)
    sum+=*(var+i);
  
  printf("The sum is: %d\n", sum);

  free(var);

  return 0;
}

/* i can not figure out why i am getting weird outputs. Is it beacuse i am 
   filling the array wrong?*/
