/*********************************************************/
/* Helen Chan                                            */
/* Assignment p12.c                                      */
/* Due February 20, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include<stdio.h>
#include<math.h>

int main(int argc, char*argv[ ])

{

  float number, h;
  printf("Enter any number!\n ");
  scanf("%f", &number);

  h=sqrt(number);
  printf("The square root of the number you entered is %f\n" , h);

  return 0;

}
