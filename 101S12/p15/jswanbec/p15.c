/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 15: Solid Box of Asterisks        */
/*                                           */
/* Approximate completion time: 14 minutes   */
/*********************************************/

#include <stdio.h>

int main()
{
  int V;
  int H;
  int x;
  int y;
  printf("Assign a value for number of rows: ");
  scanf("%d",&V);
  printf("Assign a value for number of columns: ");
  scanf("%d",&H);
  for(y=0;y<V;y++)
    {
      for(x=0;x<H;x++)
	{
	  printf("*");
	}
      printf("\n");
    }
  return 0;
}
