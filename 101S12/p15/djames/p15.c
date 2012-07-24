/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 15: Solid Box of Asterisks          */
/*                                            */
/*Approximate completeion time: 20 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int L, H, z, w;

  printf( "enter two positive integers\n" );

  scanf( "%d %d", &L, &H);

  putchar( '\n' );

  for( w=0; w<H; w++){

    for( z=0; z<L; z++ ){

      printf( "*" );

  }
    putchar( '\n' );

  }

  putchar( '\n' );

  return 0;
}
