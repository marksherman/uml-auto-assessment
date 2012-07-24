/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 38: Recursive Digit Sum               */
/* Approximate completion time: 95 mins          */
/*************************************************/

#include <stdio.h>

int dig(int num);

int main( int argc, char *argv[]){
  int num;
  FILE *fin;
  fin = fopen (argv[1], "r" );
  while( fscanf( fin, "%d", &num) != EOF )
    printf( "The sum of the digits of %d is: %d\n", num, dig(num) );
  fclose(fin);
  
  return 0;
}

int dig(int num)
{
  if( num < 10 )
    return num;
  else
    return ( num % 10 ) + dig( num / 10 );
}
