/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: sine Function                                                */
/*                                                                       */
/* Approximate completion time: 30 minutes                               */
/*************************************************************************/

#include <stdio.h>

#include <stdlib.h>

#include <math.h>

int main( int argc, char* argv[] ){
     
	double num;

	num = atof( argv[1] );
	
	printf( "The sine of %g is %g.\n", num, sin( num ) ); 
 
return 0;

}
