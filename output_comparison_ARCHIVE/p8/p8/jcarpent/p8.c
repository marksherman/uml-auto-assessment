/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program 8: One Horizontal Line of Asterisks                           */
/*                                                                       */
/* Approximate completion time: 30 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){
     
	int num, x;

	FILE* fin;

	fin = fopen( "testdata8" , "r" );

	fscanf( fin, "%d", &num);
	
	fclose( fin );
	
	if( num < 0 || num > 30 )
		
		printf( "Number on file is not between 0 and 30 inclusive.");

	else 
		for( x = 1 ; x <= num; x ++ )
		
		printf( "*" );

	printf( "\n" );
	
return 0;

}
