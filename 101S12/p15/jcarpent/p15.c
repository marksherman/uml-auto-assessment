/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: Solid Box of Asterisks                                       */
/*                                                                       */
/* Approximate completion time: 50 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){
     
	int x, y, num1, num2;

        printf( "Please enter two positive integers less than 21.\n" );

	scanf( "%d%d", &num1, &num2 );
	
	while( num1 > 20 || num2 > 20 || num1 <= 0 || num2 <= 0 ){ 

		printf( "Please enter POSITIVE integers LESS than 21.\n" );
		
		scanf( "%d%d", &num1, &num2 );
	}

	for( y = 1; y <= num2; y++ ){
			
		for( x = 1; x <= num1; x++ )

			printf( "*" );
			
		putchar( '\n' );
		
	}
        
	return 0;

}
