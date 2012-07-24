/***********************************************/
/* Programmer: Joanna Sutton                   */
/*                                             */
/* Assignment: Malloc up Space...integers      */
/*                                             */
/* Approximate Completion Time: 15 minutes     */
/***********************************************/
#include <stdio.h>
#include <stdlib.h>
int sum(int* x, int n);

int main (int arc, char* argv[]){
  int n;
  int i;
  int sum1;
  int* intarray;

  printf("Please enter a single integer\n");
  scanf("%d",&n);
  
  intarray=(int*)malloc(n*sizeof(int));

  for(i=0;i<n;i++){
    printf("Please enter a single integer\n");
    scanf("%d", &intarray[i]);
  }
  
  sum1=sum(intarray, n);

  printf("The sum of those numbers is %d\n", sum1);

  return 0;

}

int sum(int* intarray, int n){
  int sum=0;
  int i;

  for(i=0;i<n;i++){
    sum+=intarray[i];
  }
  
  return sum;

}
