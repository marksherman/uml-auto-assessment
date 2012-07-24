/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 16: Count Characters                */
/*                                            */
/*Approximate completeion time: 30 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int x=0;

  printf( "enter a string\n" );

  while( getchar() !=EOF )
    x++;
 
  putchar( '\n' );

  printf( "The number of characters counted is %d\n", x);

  return 0;
}
