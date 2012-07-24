/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 2: Bigger than 100?            */
/* Approximate completion time: 15 mins   */
/******************************************/

#include<stdio.h>
int main()
{
  int X;

  printf( "Enter a number: ");
  scanf( "%d:" , &X);
  if( X>100) 
    printf("The number is bigger than 100\n");
  else
    printf( "The number is not bigger than 100\n");
 
  return 0;
}
