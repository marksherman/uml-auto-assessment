/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 20: The Reverse Command Line        */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int j;

  for( j=argc-1; j>=0; j-- ){

    printf( "%s\n", argv[j] );
  }

  return 0;
}
