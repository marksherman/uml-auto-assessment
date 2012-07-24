/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 32: Non - Recursive Factorial                                  */
/*                                                                        */
/* Approximate Completion Time: 20 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int fact( int x );

int main( int argc, char *argv[] ) {

	int x , y ;

	printf("\nEnter a Number: ");

	scanf("%d", &x);

	y = fact( x );

	printf("\nThe factorial of %d is %d.\n\n", x , y );

  return 0 ;

}

int fact( int x ){

	int i;

	if( (x == 1) || (x == 0) )

		return 1;

	else {
		for(i = (x - 1) ; i > 0 ; i --){

			x = x * i ; 
		}

		return x;

	}

}
