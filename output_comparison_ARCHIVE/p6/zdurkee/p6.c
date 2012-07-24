/****************************************************/
/*  Programmer: Zachary Durkee                      */
/*                                                  */
/*  Program 6: Equal to Zero?                       */
/*                                                  */
/*  Approximate completion time: 30 minutes         */
/****************************************************/

#include<stdio.h>

int main( int argc, char *argv[] )

{

  int x;

  printf("Enter a Number: \n");

  scanf( "%d", &x );

  if( x == 0) {

    printf("The number is equal to zero.\n");

  } else {

    printf("The number is not equal to zero.\n");
  }
  
  return 0;

}
