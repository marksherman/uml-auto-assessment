/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #41: 1-Dimensional Array with Malloc          */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int n, i, sum = 0;
  int* ptr;

  printf("Type the number of integers you will enter: ");
  scanf("%d", &n);

  ptr = (int*)malloc(n * sizeof(int));

  for(i = 0; i < n; i++){
    printf("Type an integer: ");
    scanf("%d", ptr);
  }

  for(i = 0; i < n; i++){
    sum += ptr;
  }

  printf("%d\n", sum);
  
  free(ptr);
  
  return 0;
}
