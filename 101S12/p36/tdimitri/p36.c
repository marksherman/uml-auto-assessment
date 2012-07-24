/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 36: Persistence of a Number           */
/* Approximate completion time: 5 hours          */
/*************************************************/

#include <stdio.h>

int product( int num );

int main( int argc, char *argv[]){
  int num;
  
  printf( "Enter positive integers to find their persistence:\n" );
  while( scanf( "%d", &num ) != EOF )
    printf( "The persistence of %d is: %d\n", num, product( num ) );
  
  return 0;
}

int product(int num)
{
  int i = 0, digit, times = 1;
  
  while( num > 9 ){
    times = 1;
    do{
      digit = num % 10;
      times *= digit;
      num = num / 10;
    }while( num > 0 );
    i++;
    num = times;
  }
  return i;
}
