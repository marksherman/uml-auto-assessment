/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Recursive Digit Sum    */
/*                                 */
/* Approx Completion Time:30 min   */
/***********************************/

#include <stdio.h>


int digitsum( int n );

int main( int argc, char * argv[]){
	int n;
	FILE * fin;
	fin = fopen( argv[1], "r");


	while(fscanf( fin, "%d", &n) != EOF){
		printf( "The sum of %d is %d. \n",n, digitsum(n));

	}
	fclose( fin );

	return 0;

}

int digitsum( int n ){
	
	if( n < 10 )              /* Basecase*/
		return n;
	
	return ( n % 10 ) + digitsum( n / 10 );/* This divides the last number and adds it to n which is the last number found from modulas*/
}
