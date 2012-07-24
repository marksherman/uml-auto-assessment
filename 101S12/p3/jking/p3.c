/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 3: The Sum of Two Values                               */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/
#include<stdio.h>

int main(){
  
  int x, y, sum;

  printf( "Enter the two numbers you wish to add together:\n" );
  scanf( "%d%d", &x, &y );
  sum = x + y;
  printf( "The sum of the two numbers you have entered is %d\n", sum );  

  return 0;
}
