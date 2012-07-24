/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: Area of a Rectangle                                          */
/*                                                                       */
/* Approximate completion time: 15 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){
     
        double x, y, area;

	printf( "Please enter a value for the length of your rectangle.\n" );

	scanf( "%lg", &x );

	printf( "Please enter a value for the height.\n" );
	
	scanf( "%lg", &y );

	area = x * y;

	printf( "The area of your %lg by %lg rectangle is %lg.\n", x,y ,area );
	
	return 0;

}
