/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #20: Reverse the Command Line                 */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])

{

  int i;

  for(i = argc; i >= 0; i--){
    printf(argv[i]);
    printf("\n");
  }

  return 0;
}
