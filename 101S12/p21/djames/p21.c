/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 21: scanf returns what?             */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  FILE* fin;

  int x=0;  

  fin = fopen( "testdata21", "r" );
  
  while( fscanf( fin, "%d", &x ) != EOF )

    printf( "%d\n", x );

  fclose( fin );

  return 0;
}
