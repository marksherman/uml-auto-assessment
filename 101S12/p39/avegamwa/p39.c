/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p39: Recursive Persistence     */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int persistence(int y);
int main (int argc, char *argv[])
{

  int x;
  int value = 0;
  printf("Please Enter An integer\n");
  scanf("%d", &x);

  value = persistence(x);
  printf("The persistence number of %d is %d. \n", x, value);

  return 0;

}

int persistence( int y ){

  int prod = 1 ;

  if ( y <= 9 ) return 0 ;

  while( y != 0 ) {
    prod = prod * ( y % 10 ) ;
    y = y / 10 ;
  }

  return 1 + persistence( prod ) ;
}
