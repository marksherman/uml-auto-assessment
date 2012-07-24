/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 17: Area of a Rectangle               */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include <stdlib.h>
#include <stdio.h>
int main(int argc, char* argv[]) {
  float H, L;
  printf( "Enter two values to get the area of that rectangle: \n");
  scanf("%f%f", &H, &L);
  printf("\nThe area of that rectangle is: %f", H*L);
  putchar('\n');
  return 0;
}
