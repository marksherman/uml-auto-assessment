
/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Recursive Persistence  */
/*                                 */
/* Approx Completion Time: 4 hours */
/***********************************/


#include<stdio.h>

int digits( int a );

int persistence( int n, int i );

void get_input();

int main( int argc, char * argv[] ){
     	
	int x,i;
	
        printf( "Enter a number to find its persistence:" );
	
        if(scanf( "%d",&x) == EOF ){
		return EOF;
	}
	else {
		printf( "Persistence is:%d", persistence(x,i) );
     		printf( "\nPlease enter another number:%d\n", i );
	}
	void get_input();{
		return 0;
	}
}

int persistence( int n, int i ){
	
	if( n <= 9 ){
		n = digits(n);
		i++;
	}
	return i;
}

int digits( int a ){
	
	int product = 1;
	
	if( a == 0 ){
		return 1;
	}
	else{
		product *= a % 10;
		a = a / 10;
	}
	return product + digits(a);
}
