/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 25: Unfilled Box                                 */
/*                                                          */
/* Approximate completion time: 15 minutes                  */
/************************************************************/

#include <stdio.h>


int main(int argc, char* argv[])
{
  int L = 0, H = 0, i, j;

  printf("Input two positive integers: ");
  scanf("%d %d", &L, &H);

  for(i=1;i<=H;i++)
  {
    for(j=1;j<=L;j++)
    {
      if((i!=1&&i!=H)&&(j!=1&&j!=L))
	putchar(' ');
      else
	putchar('*');
    }
    putchar('\n');
  }

  return 0;
}
