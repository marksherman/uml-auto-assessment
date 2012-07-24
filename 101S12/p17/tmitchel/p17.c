/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 17: Area of a rectangle                 */
/*  Aproximate Completion time: 28 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] ){

  float length , height , area;

  printf ( "I calculate the area of rectangles!\n" );

  printf ( "Enter the length of your rectangle\n" );
  scanf ( "%f" , &length );

  printf ( "Enter the length of your rectangle\n" );
  scanf ("%f" , &height );

  area = length * height;

  printf ( "Area = %f\n\n " , area );

  return 0;

}

