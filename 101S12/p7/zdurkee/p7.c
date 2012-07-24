/***********************************************/
/*  Programmer: Zachary Durkee                 */
/*                                             */
/*  Program 7: Positive, Negative, or Zero?    */
/*                                             */
/*  Approximate completion time: 20 minutes    */
/***********************************************/

#include<stdio.h>

int main(){

  int x;

  printf("Enter a Number:\n");

  scanf( "%d", &x);

  if (x > 0){

    printf("The number is positive.\n");

  }

  else if (x == 0) {

    printf ("The number is zero.\n");

  }

  else {

    printf("The number is negative.\n");

  }

  return 0;

}
