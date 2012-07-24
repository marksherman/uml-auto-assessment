/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : One dimensional Array            */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>

int main (int argc, char* argv[])
{
  int array[14];
  int i=0;
  FILE *fin;
  
  
  fin = fopen ("testdata26", "r");  /*opens the file testdata 26 */
  
  
  for (i = 0; i < 15; i++)
    fscanf (fin, "%d", &array[i]); /* reads the integer values from file */

  
  for (i=14; i >= 0; i--)
    printf ("%d ", array[i]);
  /* prints out the array in reverse order */
  
  putchar ('\n');  /* adds a new line at the end of the line */
  
  return 0;
}

