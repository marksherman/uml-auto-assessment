/*************************************************/
/*  Programmer: Zachary Durkee                   */
/*                                               */
/*  Program 5: Bigger than 100?                  */
/*                                               */
/*  Approximate completion time: 15 minutes      */
/*************************************************/

#include<stdio.h>

int main( int argc, char *argv[] )

{

  int x;

  printf("Enter a Number: \n");

  scanf( "%d", &x );

  if( x > 100){

    printf("The number is bigger than 100.\n");
 
  } else { 

    printf("The number is not bigger than 100.\n");

  }

return 0;

}
