/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 15: Recursive Factorial                  */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int fact( int x );
int main( int argc, char* argv[] ){

  int x = 0;
  int y = 0;

  printf("\nEnter a number greater than 0:\n");
  scanf("%d", &x);

  if( x < 0 ){
    printf("\nThe number is less than zero, please enter another:\n");
    scanf("%d", &x);
  }
  else{
    y = fact( x );
  }

  printf("\nThe factorial of %d is %d.\n", x, y);

  return 0;

}

int fact ( x ){

  if ( x == 1 ){
    return 1;
  }
  else{
    return x * fact( x - 1 );
  }
}
    
