/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program 12: Using the sqrt Function                                   */
/*                                                                       */
/* Approximate completion time: 30 minutes                               */
/*************************************************************************/

#include <stdio.h>

#include <math.h>

int main( int argc, char* argv[] ){       

    	double num;

	printf( "Please enter a decimal number.\n" );
	
	scanf( "%lg", &num );   

  	printf( "The square root of your number is %lg.\n", sqrt( num ));
	  
return 0;

}
