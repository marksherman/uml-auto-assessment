/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Non-Recursive Factorial                                      */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/


#include <stdio.h>


int fact( int num ) ;

int main( int argc , char* argv[] ) {

	int num ;

	printf( "Factorial of what number?\n" ) ;
	scanf( "%d" , &num ) ;
	num = fact( num ) ;
	printf( "The factorial of your number is %d\n" , num ) ;
	return 0 ;
}

int fact( int num ) {
  
	int facto = 1 ;
	for ( ; num >= 1 ; num-- ) 
		facto *= num ;
	return facto ;
}
