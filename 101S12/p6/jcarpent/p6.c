/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program: Equal to Zero?                                               */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){
     
	int num;

	printf( "Please enter a number via the keyboard.\n" );

	scanf( "%d", &num );
			
       	if( num !=0 )
		
		printf( "The number is not equal to zero.\n" );

	else 
		
		printf( "The number is equal to zero.\n" );
       	  
return 0;

}
