/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : recusive digit sum	   */
/*Completion time:	15min	   */
/***********************************/

#include<stdio.h>

int digsum( int num, int sum );

int main( int argc , char* argv[]){

	FILE* fin;
	int y,x,i = 0;
	const int summ = 0;

	fin = fopen( argv[1], "r");

	while ( fscanf( fin , "%d" , &x ) != EOF ){
		i++;

		y = digsum( x , summ );

		printf( "\nThe %dth digit sum is: %d\n" ,i, y);
	}

	fclose(fin);
	return 0;
}

int digsum( int num, int sum ){

	int temp;
	
	if ( num != 0 ){
	temp = num%10;
	sum += temp;
	num /= 10;
	
	return digsum( num , sum );
	}
	else return sum;
}
