/*
Jake Conaty
Project 17
*/

#include <stdio.h>

int main(int argc, char* argv[]){

  double x, y, z;

  printf("Greetings, this program will give you the area of a rectangle of the values you give me. First, please insert the length of your rectange:\n");
  scanf("%lf", &x);

  printf("Now please insert the height:\n");
  scanf("%lf", &y);

  z=x*y;

  printf("Thank you. The area of your rectangle is %lf.\n", z);

 return 0;
}
