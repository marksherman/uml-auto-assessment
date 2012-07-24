/*********************************************************/
/*                    Betty Makovoz                      */
/*Malloc up Space for a 1-Dimensional Array of n integers*/
/*                     20 minutes                        */
/*********************************************************/ 

# include <stdio.h>
# include <stdlib.h>

int main( int argc, char* argv[]){
  int x=0;
  int i=0;
  int *int_ptr;
  int sum =0;
  printf( "Please enter the size of the array:");
  scanf( "%d", &x);
  int_ptr=(int*)malloc(x*sizeof(int));

  for (i=0; i<x; i++){
    printf("Please enter a number:");
    scanf("%d",&int_ptr[i]);
  }
  for (i=0; i<x ; i++){
    sum = sum + int_ptr[i];
  }
  printf("The sum of all the elements of the array is: %d\n",sum);
  free (int_ptr);
  return 0;
}

