/***********************************************/
/* Programmer: Jimmy Swanbeck                  */
/*                                             */
/* Program 8: One Horizontal Line of Asterisks */
/*                                             */
/* Approximate completion time: 10 minutes     */
/***********************************************/

#include <stdio.h>

int main()
{
  int x;
  int i;
  FILE* fin;
  fin=fopen("testdata8","r");
  fscanf(fin,"%d",&x);
  for(i=0;i<x;i++)
    {
      printf("*");
    }
  printf("\n");
  fclose(fin);
  return 0;
}
