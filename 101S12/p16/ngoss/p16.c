/*************************************************/
/* Programmer: Nathan Goss                       */
/*                                               */
/* Program 16: Count Characters                  */
/*                                               */
/* Approximate completion time:                  */
/*************************************************/


#include <stdio.h>


int main(int argc, char* argv[])
{
  int count=0, inchar;

  printf("Enter any characters, EOF when done:");

  while(inchar != EOF)
    {
      inchar = getchar();
      count++;
    }

  printf("You entered %d characters.\n", count);

  return 0;
}
