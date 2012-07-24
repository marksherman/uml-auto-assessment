/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 17: Area of a Rectangle             */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  double L, H;

  printf( "please enter the Length of a rectangle\n" );
  
  scanf( "%lf", &L );

  printf( "now enter the Height of a the rectangle\n" );

  scanf( "%lf", &H );

  printf( "The area of the rectangle is %f\n", L*H);

  return 0;
}
