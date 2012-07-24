#include <stdio.h>
#include <math.h>

int main(int argc, char* argv []){

  int x;
  printf("Enter any number:\n");
  scanf("%d", &x);

  x=sqrt(x);


  printf("The square root of your number is %d.\n", x);

  return 0;
}
