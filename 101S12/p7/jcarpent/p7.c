/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program 7: Positive, Negative, or Zero?                               */
/*                                                                       */
/* Approximate completion time: 15 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){
     
	int num;
	
	printf( "Please enter a number.\n" );

	scanf( "%d", &num );
	
	if( num == 0 )

		printf( "The number is zero.\n" );
	
	else if( num < 0 )
			
			printf( "The number is a negative number.\n" );
                
                else 
			
			printf( "The number is a positive number.\n" );

return 0;

}
