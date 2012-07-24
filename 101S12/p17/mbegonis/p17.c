/**********************************************************************************************************************/
/*                                                                                                                    */
/*  Mike Begonis                                                                                                      */ 
/*  Program p17                                                                                                       */
/*  This program will take the legnth and height of a rectangle inputed from the user and calculate the area of it.   */
/*  Approx Completion Time: 15 minutes                                                                                */
/**********************************************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  double x,y,a;
  
  printf("Please enter the legnth of your rectangle:\n");
  scanf("%lf",&x);
  printf("Good job I'm proud of you.\nNow can you please enter the height of your rectangle:\n");
  scanf("%lf",&y);
  a=x*y;
  printf("Thats a good boy.  The area of your rectangle is %lf.\n",a);

  return 0;
}
