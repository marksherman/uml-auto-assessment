/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 19: Argv                            */
/*                                            */
/*Approximate completeion time: 5 minutes     */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int x;

  for( x=0; x<argc; x++ ){
    
    printf( "%s", argv[x] );

    putchar( '\n' );
  }

  return 0;
}
