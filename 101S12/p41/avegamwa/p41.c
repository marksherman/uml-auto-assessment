/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p41: Malloc                    */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int *ptr_var;
  int i;
  int array[i];
  int x;
  printf("Please enter integers \n");

  ptr_var = (int *) malloc(x * sizeof(int));

  if (ptr_var == 0){

    printf(" ERROR \n");
    return 1;
  }

  *ptr_var = 0;
  x = 0;
  while(x < array[i]){
    scanf( "%d", &array[i]);
    *ptr_var = *ptr_var + array[i];
    x++;
  }

  printf( "the sum is %d \n", *ptr_var);
  free(ptr_var);

  return 0;
}
