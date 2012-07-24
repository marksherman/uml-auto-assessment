/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Area of a Rectangle               */
/*                                              */
/*     Time to Completion: 15 minutes           */
/*                                              */
/************************************************/

#include<stdio.h>

int main(int argc, char *argv[]){

  float l,h;

  printf("Enter the length of the rectangle:");

  scanf("%f",&l);

  printf("Enter the height of the rectangle:");

  scanf("%f",&h);

  printf("The area of the rectangle is:%f \n",h*l);

  return(0);
}
