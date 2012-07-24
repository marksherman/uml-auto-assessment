/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 41: Malloc up Space             */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int *ptr_var;
  int array[4];
  int n;
  printf("Please enter 4 integers \n");
  
  ptr_var = (int *) malloc(n * sizeof(int));
    
  *ptr_var = 0;
  n = 0;
  while(n < 4){
    scanf( "%d", &array[n]);
    *ptr_var = *ptr_var + array[n];
    n++;
  }
  
  printf( "the sum is %d \n", *ptr_var);
  free(ptr_var);
  
  return 0;
}










