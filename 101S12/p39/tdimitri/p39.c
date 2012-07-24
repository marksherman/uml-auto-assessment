/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 39: Persistence of a Number           */
/* Approximate completion time: 3 hours          */
/*************************************************/

#include <stdio.h>

int product( int num );
int pers( int num, int i );

int main( int argc, char *argv[]){
  int num;
  
  printf( "Enter positive integers to find their persistence:\n" );
  while( scanf( "%d", &num ) != EOF )
    printf( "The persistence of %d is: %d\n", num, pers( num, 0 ) );
  
  return 0;
}

int product(int num)
{
  if( num < 10 )
    return num;
  else
    return ( num % 10 ) * product( num / 10 );
}

int pers( int num, int i )
{
  if( num < 10 ) 
    return i;
  else
    return pers( product( num ), ++i );
}
