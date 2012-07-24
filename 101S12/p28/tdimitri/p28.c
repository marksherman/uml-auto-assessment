/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 28: Digit Sum                         */
/* Approximate completion time: 90 mins          */
/*************************************************/

#include <stdio.h>

int dig(int num);

int main( int argc, char *argv[]){
  int num;
  FILE *fin;
  fin = fopen (argv[1], "r");
  while( fscanf( fin, "%d", &num) != EOF)
    printf( "The sum of the digits is: %d\n", dig(num));
  fclose(fin);
  
  return 0;
}

int dig(int num)
{
  float t;
  int sum = 0;
  if(num < 10)
    sum = num;
  else{
    while(num >= 10){
      t = num % 10;
      num = (num / 10);
      sum = t + num;
    }
  }
  return sum;
}

