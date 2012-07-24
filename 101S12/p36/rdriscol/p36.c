/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Persistance of a Number*/
/*                                 */
/* Approx Completion Time:1 hr     */
/***********************************/

#include<stdio.h>


int persistence( int n );


int main(int argc, char * argv[]){


	int x;
	


	printf( "Enter a number to find its persistence:" );

	while(scanf(" %d",&x) != EOF ){

		printf( "Persistence is:%d", persistence(x) );
		printf( "\nPlease enter another number:\n" );
	}

	
	return 0;

}

int persistence( int n ){

	int count = 0;
	int product = 1;
	

	while( 0 != n / 10 ){
		while( n > 0){
			product *= n % 10;
			n = n / 10;

		}
		count++;
		n = product;
		product = 1;
	}
	return count;
	
}


/* need %10 in here somewhere.*/
