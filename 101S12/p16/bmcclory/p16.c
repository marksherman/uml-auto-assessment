/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #16: Count Characters                         */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])

{

  int ch, i;

  printf("Type a string. Press control + C twice when finished: ");
  
  for(i = 0, getchar() != EOF; ; i++){
    ch = getchar();
  }

  putchar(i);

  return 0;
}
