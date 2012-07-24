/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 23: fgetc and toupper               */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>
#include <ctype.h>
int main(int argc, char* argv[]){  

  FILE* fin;

  int x=0;

  fin = fopen( "testdata23", "r" );

  while( (x = fgetc(fin)) != EOF ){

    x = toupper(x);

    putchar( x );

  }

  putchar( '\n' );

  return 0;
}
