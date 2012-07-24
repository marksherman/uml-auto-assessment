/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : Simulate call reference*/
/*Completion time:	10min	   */
/***********************************/

#include<stdio.h>

void swap( int* a, int* b );

int main( int argc , char* argv[] ){

  int x,y;

  printf( "Enter first then second integer\n" );

  scanf( "%d %d", &x,&y );

  printf( "\nfirst intput %d , second input %d\n", x, y);

  swap( &x, &y );

  printf("\nswapped first input %d , swapped second %d\n", x,y);

  return 0;

}

void swap( int* a, int* b){

  int temp;

  temp = *a;

  *a = *b;

  *b = temp;

  return;
}
