/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 16 : count characters           */
/*                                         */
/* Approximate completion time:120 minutes */
/*******************************************/

#include <stdio.h>
int main()
{
  int c, count;
  printf("Enter characters: ");
  c = getchar();
  while ((c= getchar()) != EOF)
    {
      count++;
    }
  printf("%d characters\n", count);

  return 0;  
}
