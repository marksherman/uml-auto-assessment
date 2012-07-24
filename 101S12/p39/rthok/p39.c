
/****************************************************************/
/*                                                              */
/* Programmer: Ravy Thok                                        */
/*                                                              */
/* Program 39: Recursive Persistence                            */
/*                                                              */
/* Approximate Completion Time: 240 minutes                      */
/*                                                              */
/****************************************************************/

#include <stdio.h>

int scan( int num ) ;

int per( int num, int count ) ;

int digitproduct( int num ) ;

int main( int argc, char *argv[] ) {

	int num ;

	printf( "\nEnter a number: " ) ;

	if( scanf( "%d", & num )== EOF ){

	printf( "\nEnd Of File\n" ) ;

	return 0 ;

	}

	else{
   
		num = scan( num ) ;

		return 1 ;
	
	}

}

int scan( int num ){

	int result = 0 ;	

	result = per( num, 0 ) ;
	
	printf( "The persistence of %d is %d.\n", num , result ) ;
		
	printf( "\nEnter another number or an EOF: " ) ;

	scanf( "%d", &num ) ;

	num = scan( num ) ;

	if( num == EOF ){

		printf( "\nEnd Of File\n\n" ) ;

		return 0 ;       
	}

	else{

		num = scan ( num ) ; 

		return 0 ;

	}
 
}

int per( int num , int count ){

	if ( num >= 10 ){

		count ++ ;

		num = digitproduct( num ) ;	       

		if ( num >= 10 ){

			num = per( num, count ) ;
           
			return num ;
		}

	}

		return count ;

}

int digitproduct( int num ){

	int x = 0 ;

	x = num % 10 ;

	num = num / 10 ;

	if( num >= 10 ){

		num = digitproduct( num ) ;

		return ( num * x );
	}


	else 

		return ( num * x ) ;
}
