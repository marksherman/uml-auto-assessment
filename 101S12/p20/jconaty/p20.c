/*****************************/
/*Jake Conaty                */
/*project 20                 */
/*                           */
/*****************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int x;

  for(x=argc-1; x>=0; x--){

    printf("%s\n", argv[x]);
  }

  return 0;

}
