/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: Area of a Circle                                             */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/

#include <stdio.h>

#include <math.h>

int main( int argc, char* argv[] ){
     
	double rad, area;

	printf( "Please enter the radius of your circle.\n" );
	
	scanf( "%lg", &rad );

	area = M_PI * rad * rad;
	
	printf( "The area of a circle with radius %lg is %lg.\n", rad, area );

	return 0;
	
}
