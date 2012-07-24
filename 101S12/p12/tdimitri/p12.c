/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 12: Using the sqrt Function    */
/* Approximate completion time: 10 mins   */
/******************************************/
#include<stdlib.h>
#include<stdio.h>
#include<math.h>
int main()
{
  float X;
  printf( "Enter a number to find its square root value: ");
  scanf( "%f:" , &X);
  printf("The square root value of the number you entered is:%f\n", sqrt(X));
 
  return 0;
}
