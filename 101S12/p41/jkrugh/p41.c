/***********************************************************************/
/* Programmer: Jeremy Krugh                                            */
/* Program 41: Malloc up Space for a 1-Dimensional Array of n Intergers*/
/* Approximate completion time: 40 minutes                             */
/***********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int n;
  int i;
  int* x;

  scanf("%d", &n);

  x = (int*)malloc(n*sizeof(int));
  for(i = 0; i < n; i++){
    scanf("%d",x+1);
}
  printf("The sum of the number is: %d\n", *x);

  free(x);
  return 0;
}

/*i cant figure out why the output is zero every time*/
