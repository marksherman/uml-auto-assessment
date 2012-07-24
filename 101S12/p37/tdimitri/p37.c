/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 37: Digit Sum                         */
/* Approximate completion time: 95 mins          */
/*************************************************/

#include <stdio.h>

int dig(int num);

int main( int argc, char *argv[]){
  int num;
  FILE *fin;
  fin = fopen (argv[1], "r");
  while( fscanf( fin, "%d", &num) != EOF)
    printf( "The sum of the digits of %d is: %d\n", num, dig(num));
  fclose(fin);
  
  return 0;
}

int dig(int num)
{
  int t = 0, sum = 0;
 
  while( num >= 10 ){
    t = num % 10;
    num = (num / 10);
    sum+= t;
  }
  if( num < 10 )
    sum+= num;

  return sum;
}
