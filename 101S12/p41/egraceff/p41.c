/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Malloc up Space for a 1-Dimensional Array of n integers          */
/*                                                                           */
/* Approximate completion time: 45 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
int main( int argc, char *argv[] )
{
  int* ptr;
  int n, i, sum = 0;
  printf("Please enter the number of integers you wish to sum: \n");
  /*prompts the user for the length of the array */
  scanf("%d", &n);
  /* stores the length of the array into the variable n */
  ptr = (int*)malloc(n*sizeof(int));
  /* uses malloc to allocate memory for an array of length n */
  printf("Please enter the %d integers you wish to sum:\n", n);
  /* prompts the user for the values to be stored into said array */
  for (i=0; i<n; i++){
    scanf("%d", &ptr[i]);
    sum = sum + ptr[i];
    /* scans integers into the array and takes the cummulative sum */
  }
  printf("The sum of these values is %d.\n", sum);
  /* prints out the sum of the integers found from the above loop */
  free(ptr);
  /* deallocates the memory previously allocated for the array */
  return 0 ;
}
