/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 22: Sum of a Bunch                               */
/*                                                          */
/* Approximate completion time: 5 minutes                   */
/************************************************************/


#include <stdio.h>


int main(int argc, char* argv[])
{
  FILE* fin;
  int sum=0,val;

  fin = fopen("testdata22","r");

  while(fscanf(fin,"%d",&val) != EOF)
    sum += val;

  printf("The sum is: %d\n", sum);

  fclose(fin);

  return 0;
}
