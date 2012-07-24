/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p41: Malloc 1D Array                          */
/*                                                       */
/* Approximate completion time: 20 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int size, i, sum = 0;
  int* malloc_array;
  printf("How many locations would you like the array to hold (not including a null byte): ");
  scanf("%d", &size);
  /*Ask for array size and scan in the array size.*/
  malloc_array = (int*) malloc(size * sizeof(int));
  /*Assign name malloc_array to the array function that will reserve the appropriate size of memory in the heap for user.*/
  for(i = 0; i < size; i++){
    printf("Enter the integer you want stored into 'array[%d]': ", i);
    scanf("%d", &malloc_array[i]);
    /*Asks for and scans in an interger into every memory spot available on the malloc'ed array.*/
  }
  for(i = 0; i < size; i++){
    sum = sum + malloc_array[i];
    /*Sums up all the inters in the array.*/
  }
  printf("The sum of all integers in the array is %d. \n", sum);
  /*Prints out the sum of integers.*/
  free(malloc_array);
  /*Deallocates the memory reserved for this program from the malloc.*/
  return 0;
}
