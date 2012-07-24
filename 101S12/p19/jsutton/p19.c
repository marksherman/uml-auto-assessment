/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: argv                       */
/*                                        */
/* Approximate Completion Time: 15 minutes*/
/******************************************/
#include <stdio.h>
int main(int argc, char* argv[]){
  int i;

  for (i=0;i<argc;i++){
    printf(argv[i]);
    putchar('\n');
  }
  
  return 0;

}
