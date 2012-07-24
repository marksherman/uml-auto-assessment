/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 2: Positive, Negative, or Zero?*/
/* Approximate completion time: 10 mins   */
/******************************************/

#include<stdio.h>
int main()
{
  int X;

  printf( "Enter a number: ");
  scanf( "%d:" , &X);
  if( X==0) 
    printf("The number is 0.\n");
  else if( X>0)
    printf( "The number is positive.\n");
  else
    printf( "The number is negative.\n");
 
  return 0;
}
