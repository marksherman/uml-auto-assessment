/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #19: Argv                                     */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])

{

  int i;

  for(i = 0; i < argc; i++){
    printf(argv[i]);
    printf("\n"); /* including this with the previous printf told me that there were too many arguments for formating */
  }

  return 0;
}
