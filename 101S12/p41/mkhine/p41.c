/***********************************************/
/* Program : Malloc up Space for a 1-Dimensional Array of n integers   (p40)    */
/*                                             */ 
/* Programmer : Min Thet Khine                 */
/*                                             */
/* Approximate complement time : 25 mins       */
/***********************************************/
#include<stdio.h>
#include <stdlib.h>

int main(void){
  int a;
  int i;
  int* array;
  int sum=0;
  printf("Please enter the number of integers.");
  scanf("%d", &a);

  array= (int*)malloc(a*sizeof(int));  /* allocating storage for the array */
  printf("Please enter the numbers to sum up.");
  for(i=0; i<a ; i++){
 
   
    scanf("%d", &array[i]);
    sum+=array[i];
    }
  printf("The sum is %d.", sum);

  free((void*)array);
  return 0;
}
