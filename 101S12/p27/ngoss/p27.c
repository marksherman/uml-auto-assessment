/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 27: Reverse                                      */
/*                                                          */
/* Approximate completion time: 6 minutes                   */
/************************************************************/

#include <stdio.h>

int main(int argc, char* argv[])
{
  int nums[10], i;

  printf("Input ten integers: ");

  for(i=0;i<10;i++)
  {
    scanf("%d", &nums[i]);
  }

  for(i=9;i>=0;i--)
  {
    printf("%d ", nums[i]);
  }

  printf("\n");

  return 0;
}
