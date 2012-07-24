/*                                   */
/* Programmer: Rachel Driscoll       */
/*                                   */
/* Title: Argc                       */
/*                                   */
/* Approx Completion Time: 30 min    */
/*                                   */

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[]){

  int i, x;
 
  printf("argc: %d\n", argc);

  for(i=0; i< argc; i ++){
       atoi ( argv [1] );
  }
    printf("%d\n", argv[1]);
    
    return 0;
}
