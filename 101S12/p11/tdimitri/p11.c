/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 11: The abs Function           */
/* Approximate completion time: 10 mins   */
/******************************************/
#include<stdlib.h>
#include<stdio.h>
int main()
{
  int X;
  printf( "Enter a number to find its absolute value: ");
  scanf( "%d:" , &X);
  printf("The absolute value of the number you entered is:%d\n", abs(X));
 
  return 0;
}
