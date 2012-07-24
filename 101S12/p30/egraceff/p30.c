/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Simulating Call By Reference                                     */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
void swap(int *a, int *b);
int main( int argc, char *argv[] )
{
  int x, y;
  printf("Please enter the two numbers you wish to swap:\n");
  /* prompts the user for the two values */
  scanf("%d %d", &x, &y);
  /* stores the values into the variables x and y */
  swap(&x, &y);
  /* the function swap is called */
  printf("%d %d\n", x, y);
  /* the swapped values within the original variables of x and y are printed */
  return 0;
}
void swap(int *a, int *b)
{
  int temp;
  temp = *a;
  /* temp is used to store the current value of x */
  *a = *b;
  /* let x now contain the value stored in y */
  *b = temp;
  /* now replace the original value of x, held in temp, into y */
  return ;
  /* Because the values of x and y were called by reference, there is no 
     need for this function to return anything. All necessary changes were 
     made in the main function. */
}
