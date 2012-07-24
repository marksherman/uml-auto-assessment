/*************************************************/
/* Programmer: Nathan Goss                       */
/*                                               */
/* Program 17: Area of a Rectangle               */
/*                                               */
/* Approximate completion time: 4 minutes        */
/*************************************************/



#include <stdio.h>


int main(int argc, char* argv[])
{
  double hi, len;

  printf("Input two floating point numbers for length and height: ");
  scanf("%lf %lf", &hi, &len);

  printf("The area of the rectangle is %lf\n", hi * len);

  return 0;
}
