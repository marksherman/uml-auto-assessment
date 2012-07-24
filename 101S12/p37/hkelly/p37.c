/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 28: Digit Sum                            */
/*                                                  */
/* Approximate completion time: 60 minutes          */
/****************************************************/

#include <stdio.h>

int digitsum( int x);
int main( int argc, char* argv[] ){

  int n = 0;
  int y = 0;

  FILE* fin;
  fin = fopen(argv[1], "r");

  printf("\nThe sum of the digits of the numbers entered are: \n");
  while(fscanf(fin, "%d", &n) != EOF){
    y = digitsum(n);
    printf("%d",y);
    printf("\n");
  }

  fclose(fin);

  return 0;
}

int digitsum( int x ){

  int sum = 0;

  while( x > 0 ){
    sum += (x % 10);
    x = x/10;
  }

  return sum;
}

      
