/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 9: Using a for Loop               */
/*                                           */
/* Approximate completion time: 26 minutes   */
/*********************************************/

#include <stdio.h>

int main()
{
  int x;
  int y;
  FILE* fin;
  fin=fopen("testdata9","r");
  for(x=0;x<5;x++)
    {
      fscanf(fin,"%d",&y);
      printf("%d\n",y);
    }
  return(0);
}
