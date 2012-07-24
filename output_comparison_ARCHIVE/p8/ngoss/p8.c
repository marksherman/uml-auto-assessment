/*************************************************/
/* Programmer: Nathan Goss                       */
/*                                               */
/* Program 8: One Horizontal Line of Asterisks   */
/*                                               */
/* Approximate completion time: 3 minutes        */
/*************************************************/


#include <stdio.h>

int main(int argc, char* argv[])
{
  int i,val;
  FILE* fin;

  fin = fopen("testdata8","r");

  fscanf(fin, "%d", &val);

  for(i=0;i<val;i++)
    {
      printf("*");
    }
  printf("\n");

  return 0;
}
