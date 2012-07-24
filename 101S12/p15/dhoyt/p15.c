/***********************************/
/* Programmer: David Hoyt          */
/* Program: Box of Asterisks       */
/* Time: 20min                     */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int i, j, x = 0,  y = 0;

  printf( "Enter the length of your box:" );

     scanf( "%d", &x );

  printf( "Enter the height of your box:" );

     scanf( "%d", &y );

     for( j = 0; j < y; j++ ){     

       for( i = 0; i < x; i++ ){

       putchar( '*' );

     }
 putchar( '\n' );

     }

     return 0;

}

