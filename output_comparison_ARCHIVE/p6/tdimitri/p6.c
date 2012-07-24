/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 2: Equal to 0?                 */
/* Approximate completion time: 10 mins   */
/******************************************/

#include<stdio.h>
int main()
{
  int X;

  printf( "Enter a number: ");
  scanf( "%d:" , &X);
  if( X==0) 
    printf("The number is equal to 0.\n");
  else
    printf( "The number is not equal to 0.\n");
 
  return 0;
}
