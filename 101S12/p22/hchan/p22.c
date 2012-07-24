/*********************************************************/
/* Helen Chan                                            */
/* Assignment p22.c                                      */
/* Due March 28, 2012                                    */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>

int main (int argc, char* argv[ ])
  
{
  
  int a, 
    sum = 0;
  
  FILE *fin;
  
  fin = fopen("testdata22", "r");
  
  while (fscanf (fin, "%d", &a)!= EOF)
    sum = a + sum;
  
  printf ("The sum of the numbers of testdata22 is: %d \n", sum);
  
  return 0;
  
}
