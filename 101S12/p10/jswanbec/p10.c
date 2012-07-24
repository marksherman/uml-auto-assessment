/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 10: Sum of Twenty                 */
/*                                           */
/* Approximate completion time: 24 minutes   */
/*********************************************/

#include <stdio.h>

int main()
{
  int x;
  int y;
  int z;
  z=0;
  FILE* fin;
  fin=fopen("testdata10","r");
  for(x=0;x<20;x++)
    {
      fscanf(fin,"%d",&y);
      z=z+y;
    }
  printf("The sum is: %d\n",z);
  return(0);
}
