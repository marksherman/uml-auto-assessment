/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Area of a Circle                  */
/*                                              */
/*     Time to Completion: 15 min               */
/*                                              */
/************************************************/

#include<stdio.h>
#include<math.h>

int main(int argc,char *arcv[]){

  float r;
  
  printf("Enter the radius of the circle:");

  scanf("%f",&r);

  printf("The area is:%f \n",r*r*M_PI);

  return(0);
}
