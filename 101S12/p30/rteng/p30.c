/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 30: Simulating Call By Reference              */
/*                                                       */
/* Approximate completion time: 13 minutes               */
/*********************************************************/
#include <stdio.h>
void swap(int* a, int* b);
/*Prototypes function "swap"*/
int main(int argc, char* argv[])
{
  int x, y;
  /*Ask user to input values for x and y*/
  printf("Input an integer to be the value for x: ");
  scanf("%d", &x);
  printf("Input an integer to be the value for y: ");
  scanf("%d", &y);
  printf("\nThe initial value of x is %d.\nThe initial value of y is %d.\n\n", x, y);
  /*Prints out the intial values for x and y for user clarification.*/
  swap(&x, &y);
  /*Calls the function swap, to switch the values of x and y*/
  printf("The values are now switched.\nx is %d. y is %d.\n\n", x, y);
  /*Prints out the values of the switched variables.*/
  return 0;
}
void swap(int* a, int* b)
{
  int temp;
  temp = *a;
  *a = *b;
  *b = temp;
  /*Switches the values of inputted a and b by creating a temporary variable to store address of a and switching it with the address of b.*/ 
}
