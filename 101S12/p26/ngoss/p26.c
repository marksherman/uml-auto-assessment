/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 26: One Dimensional Array                        */
/*                                                          */
/* Approximate completion time: 5 minutes                   */
/************************************************************/


#include <stdio.h>

int main(int argc, char* argv[])
{
  int nums[15], i;
  FILE* fin;

  fin = fopen("testdata26","r");

  for(i=0;i<15;i++)
  {
    fscanf(fin, "%d", &nums[i]);
  }

  for(i=14;i>=0;i--)
  {
    printf("%d ", nums[i]);
  }
  
  printf("\n");

  fclose(fin);
  
  return 0;
}
