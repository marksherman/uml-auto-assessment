/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 20: Reverse the Command Line      */
/*                                           */
/* Approximate completion time: 3 minutes    */
/*********************************************/

#include <stdio.h>

int main(int argc,char* argv[])
{
  int i;
  for(i=argc-1;i>=0;i--)
    {
      printf(argv[i]);
      printf("\n");
    }
  return 0;
}
