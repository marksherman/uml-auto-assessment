/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Recursive Digit Sum                                          */
/*                                                                           */
/* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/


#include <stdio.h>


int sum( int x ) ;

int main( int argc , char* argv[] ) {
	
	int x , token ;
	FILE* fin ;
	
	fin = fopen( argv[1] , "r" ) ;
        while ( ( token = fscanf( fin , "%d" , &x ) ) != EOF ) {
		x = sum( x ) ;
	printf( "The sum of the digits is : %d\n" , x ) ;
	}
	fclose(fin) ;
	return 0 ;
}

int sum ( int x ) {

	int i ;

	if ( x == 0 )
		return x ;
	else {
		i = x % 10 ;
		x /= 10 ;
		return i += sum( x ) ;   
	}
}
