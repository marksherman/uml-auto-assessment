/*************************************************************************/
/* Programmers Name: John Carpenter                                      */
/*                                                                       */
/* Program 3: Sum of Two Values                                          */
/*                                                                       */
/* Approximate completion time: 35 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){

	int x, y, sum;
	
        printf( "\nWelcome to the adding machine.\n\n" );

        printf( "Please enter two numbers separated by a space.\n\n" );
	       
    	scanf( "%d%d", &x,&y );
	       
	sum = x + y;
		
	printf( "\n%d + %d = %d\n\n", x, y, sum );

return 0;

}
