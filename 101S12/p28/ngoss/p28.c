/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 28: Digit Sum                                    */
/*                                                          */
/* Approximate completion time: 30 minutes                  */
/************************************************************/


#include <stdio.h>

int getsum(int num);

int main(int argc, char* argv[])
{
  int sum = 0, val = 0;
  FILE* fin;
  
  fin = fopen(argv[1],"r");

  fscanf(fin, "%d", &val);

  sum = getsum(val);
  printf("The sum of the digits of the integer %d is %d\n", val, sum);

  fclose(fin);

  return 0;
}


int getsum(int num)
{
  /*This function takes a multi-digit number and adds its digits together*/
  int sum = 0;

  while(num>0)
  {
    sum+=(num%10);
    num-=(num%10);
    num/=10;
  }
  
  return sum;
}
