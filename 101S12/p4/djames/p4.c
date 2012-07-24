/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 4: The fscanf Function              */
/*                                            */
/*Approximate completeion time: 15  minutes   */
/**********************************************/

#include <stdio.h>
int main(){  

  FILE* fin;

  int x;

  fin = fopen( "testdata4", "r" );

  fscanf( fin, "%d", &x );

  printf( "\n%d\n\n", x );

  fclose( fin );


  return 0;
}
