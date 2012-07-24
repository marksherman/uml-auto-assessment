/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 32: Non-Recursive Factorial              */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int fact( int x );
int main( int argc, char* argv[] ){
  
  int x = 0;
  int y = 0;

  printf("\nEnter a number 0 or greater:\n");
  scanf("%d", &x);

  if( x < 0 ){
    printf("\nThe number is less than zero, enter another:\n");
    scanf("%d", &x);
  }

  y = fact( x );

  printf("\nThe factorial of %d is %d\n", x, y);

  return 0;

}

int fact( x ){

  int i;
  int n = x;
  int f = 1;

  for( i = 1; i <= n; i++ ){
    f = f*i;
  }

  return f;
}
  
