/****************************************************/
/* Programmer: Nathan Goss                          */
/*                                                  */
/* Program 9: Using a for Loop                      */
/*                                                  */
/* Approximate completion time: 4 minutes           */
/****************************************************/


#include <stdio.h>

int main(int argc, char* argv[])
{
  int i,val;
  FILE* fin;

  fin = fopen("testdata9","r");

  for(i=0;i<5;i++)
    {
      fscanf(fin, "%d", &val);
      printf("%d ", val);
    }
  printf("\n");

  return 0;
}
