/******************************/
/* Programmer: David Hoyt     */
/* Program: Count Characters  */
/* Time: 40min                */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x, i = 0;

  printf( "Enter characters, press ctrl+d when done:\n" );

  while( ( x = getchar() ) != EOF){ 

    i++;

  }

  i--;

  printf( "Number of characters: %d\n", i );

  return 0;

  }
