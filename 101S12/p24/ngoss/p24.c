/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 24: Find the Average                             */
/*                                                          */
/* Approximate completion time: 5 minutes                   */
/************************************************************/


#include <stdio.h>


int main(int argc, char* argv[])
{
  int val = 0, sum = 0, i;
  float avg;
  FILE* fin;

  fin = fopen("testdata24","r");

  for(i=0;i<4;i++)
  {
    fscanf(fin,"%d",&val);
    sum+=val;
  }

  avg = sum / 4.0;

  printf("The average is %f\n", avg);

  fclose(fin);
  
  return 0;
}
