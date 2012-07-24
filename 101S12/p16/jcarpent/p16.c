/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: Count Characters                                             */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){
     
	int num, stuff;

	num = 0;

	printf( "Please enter a few random characters.\n" );

	while( ( stuff = getchar() ) != EOF )

		num ++;
	
	printf( "\nYou typed %d characters.\n", num );

return 0;

}
