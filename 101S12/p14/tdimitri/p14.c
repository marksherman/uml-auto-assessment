/******************************************/
/* Programmer: Theodore Dimitriou         */
/* Program 14: Sine Function              */
/* Approximate completion time: 25 mins   */
/******************************************/
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int main(int argc, char* argv[])
{ 
  float x;
  x = atof(argv[1]);
  printf( "%f", sin(x));
  putchar('\n');
  return 0;
}
