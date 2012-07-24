/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 41: Malloc up space for 1D array                       */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv [] ){
   
  int n;
  int i=0;
  int filler;
  int sum=0;
  int* var_ptr;
  
  printf("Enter an integer: ");
  scanf("%d", &n);
  var_ptr=(int*)malloc(n*sizeof(int));
  
  for(i=0;i<n;i++){
    printf("Enter an integer value for var_ptr[%d]: ", i);
    scanf("%d", &filler);
    var_ptr[i]=filler;
  } 
  for(i=0;i<n;i++){
    sum=sum+var_ptr[i];
  }

  printf("The sum of the integers in the array is %d\n", sum);  
  free(var_ptr);

  return 0;   
}
