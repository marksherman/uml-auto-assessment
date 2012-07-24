/****************************************************************/
/*Programmer: Min Thet Khine                                    */
/*                                                              */
/*Program: Sum of Twenty Integer values                         */
/*                                                              */
/*Approximate Complete time: 20 minutes                         */ 
#include <stdio.h>
int main(void) 
{
  int sum=0;   /*initialize sum to 0  */
  int n, i;    /* declare variables n and i */
  FILE *fin;
  int bounds=20;   /*limit the number input to 20 */

  fin = fopen( "testdata10", "r");  /*opening the file testdata*/
  
  for (i= 0; i<bounds; i++) {   /*setting up the IF statement */
    fscanf(fin, "%d", &n);
    sum = sum+n;
  }

  printf("The sum of the 20 integers is  %d \n", sum);  /*displays the sum on the screen */
  fclose(fin);   /*closing the file testdata*/
  return 0;
}



