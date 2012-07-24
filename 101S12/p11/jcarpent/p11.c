/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program 11: The abs Function                                          */
/*                                                                       */
/* Approximate completion time: 25 minutes                               */
/*************************************************************************/

#include <stdio.h>

#include <stdlib.h>

int main( int argc, char* argv[] ){     

    	int num;

	printf( "Please enter an integer.\n" );

	scanf ( "%d", &num );

	printf( "The absolute value of your integer is %d.\n", abs( num ));
	  
return 0;

}
