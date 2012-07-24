/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Digit Sum ( again )                                          */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                  */
/*****************************************************************************/


#include <stdio.h>
#include <stdlib.h> 


int sum( int x ) ;

int main( int argc , char* argv[] ) {

	int digit , dsum , token = 0 ;
	FILE* fin ;
	
	fin = fopen( argv[1] , "r" ) ;
	while ( ( token = fscanf( fin, "%d" , &digit) ) != EOF ) {
		dsum = sum( digit ) ;
		printf( "The sum of the digits is : %d\n" , dsum ) ;
	}
	fclose(fin) ;
	return 0 ;
}

int sum( int x ) {

	int sum = 0 , modnum ;
	
	while( x != 0 ) {
		modnum = x % 10 ;
		x = x / 10 ;
		sum += modnum ;
	}
	return sum ;
}
