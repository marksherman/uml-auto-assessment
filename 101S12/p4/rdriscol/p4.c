/* Programmer: Rachel Driscoll               */
/*                                           */
/* Program 4: The fscanf Function            */
/*                                           */
/* Approximate Completion Time:40 min         */
/*                                           */

#include <stdio.h>

int main(){

  int k; 
  FILE* fin;
  fin = fopen ("testdata4", "r");
  printf (" You can print out the number stored in the file named testdata4\n ");
  fscanf( fin, "%d", &k);
  printf( "The number is %d\n",k);
  
  fclose( fin );
  
  return 0;
}  
