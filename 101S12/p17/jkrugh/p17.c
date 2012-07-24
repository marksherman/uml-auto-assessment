/**********************************************************/
/* Programmer: Jeremy Krugh                               */
/*                                                        */
/* Program 17: Area of a Rectangle                        */
/*                                                        */
/* Approximate time of completion: 20 minutes             */
/**********************************************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  int x;
  int y;
  int z;

  printf("Enter length of rectangle: ");
  scanf("%d",&x);
  printf("Enter height of rectangle: ");
  scanf("%d",&y);

  z = (x*y);

  printf("Area of retangle: %d\n",z);

  return 0;
}
