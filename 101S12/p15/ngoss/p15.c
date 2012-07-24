/*********************************************/
/* Programmer: Nathan Goss                   */
/*                                           */
/* Program 15: Solid Box of Asterisks        */
/*                                           */
/* Approximate completion time: 3 minutes    */
/*********************************************/


#include <stdio.h>


int main(int argc, char* argv[])
{
  int i, j, len, hi;

  printf("Input two non-negative integers: ");
  scanf("%d %d", &len, &hi);

  for(i=0;i<hi;i++)
    {
      for(j=0;j<len;j++)
	{
	  putchar('*');
	}
      putchar('\n');
    }

  return 0;
}
