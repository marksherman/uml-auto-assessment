/********************************************/
/* Programmer: Nathan Goss                  */
/*                                          */
/* Program 11: The abs Function             */
/*                                          */
/* Approximate completion time: 3 minutes   */
/********************************************/

#include <stdio.h>
#include <stdlib.h>


int main(int argc, char* argv[])
{
  int inval;

  printf("Input an integer: ");
  scanf("%d", &inval);

  printf("%d\n", abs(inval));

  return 0;
}
