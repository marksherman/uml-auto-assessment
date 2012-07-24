/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Reverse                          */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>

int main(int argv, char *argc[])
{
  int array[9];
  int i = 0;
  int count = 10; /* the number of integers which is read */

  
  printf ("Please enter 10 integers : "); /*prompts the user to type 10 integers */ 
  
  
  for (i = 0; i< count ; i++)
    scanf ("%d", &array[i]); /*reads integer values from the keyboard */
  
  
  for (i = count - 1; i >= 0; i--)
    printf ("%d ",array[i]); /*starts the for loop and prints the array in reverse order */
  
  putchar ('\n'); 
  return 0;
}

