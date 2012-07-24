/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Recursive Factorial                                          */
/*                                                                           */
/* Approximate Completion Time : 2 minutes                                   */
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
  
	if ( num == 1 )
		return 1 ;
	else 
		return num * fact( num-1 ) ;
}
