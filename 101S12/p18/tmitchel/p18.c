/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 17: Area of a circle                    */
/*  Aproximate Completion time: 5 Minutes           */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<math.h>

int main( int argc, char *argv[] ){

  float radius , area;

  printf ( "I calculate the area of circles!\n" );

  printf ( "Enter the radius of your circle\n" );
  scanf ( "%f" , &radius );

  area = radius * radius * M_PI;

  printf ( "Area = %f\n\n " , area );

  return 0;

}

