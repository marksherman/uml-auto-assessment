/***********************************************/
/*                                             */
/*  Mike Begonis                               */
/*  Program p41                                */
/*                                             */
/*  This program takes in n integers through   */
/*  standard output and stores them using      */
/*  malloc.  It then computes and prints out   */
/*  the sum of the numbers.                    */
/*                                             */
/*  Approx Completion Time: 10 minutes         */
/***********************************************/

#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[]){
  
  int n, i=0,sum=0;
  int* num;
  
  printf("How many numbers do you want to enter?\n");
  scanf("%d",&n);
  
  num=(int*)malloc(n*sizeof(int));
  printf("Please enter %d numbers.  Separate each number with a space.\n",n);
  
  while(i<n){
    scanf("%d",&num[i]);
    sum+=num[i];
    i++;
  }
  
  printf("The sum of the numbers is %d\n",sum);

  free(num);

  return 0;
}


