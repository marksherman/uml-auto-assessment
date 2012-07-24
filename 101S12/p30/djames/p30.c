/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 30: Simulating Call By Reference    */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

void swap( int* a, int* b );

int main(int argc, char* argv[]){  

  int x, y;

  printf( "enter two numbers\n" );

  scanf( "%d %d", &x, &y );

  swap( &x, &y );

  printf( "the numbers swapped are %d and %d\n", x, y );

  return 0;
}

void swap( int* a, int* b ){

  int tmp;

  tmp = *a;

  *a = *b;

  *b = tmp;

  return;
}
