/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  digit sum	Revised	   */
/*Completion time: 25 mins         */
/***********************************/
#include<stdio.h>

int digsum( int a );

int main( int argc , char* argv[]){

	FILE* fin;
	int y,x,i = 0;
	
	fin = fopen( argv[1], "r");

	while ( fscanf( fin , "%d" , &x ) != EOF ){
		i++;
		y = digsum( x );
  
		printf( "\nThe %dth digit sum is: %d\n" ,i, y);
	}

	fclose(fin);

	return 0;
}

int digsum( int a){

	int i,x,sum = 0;

	for ( i = 0 ; a != 0 ; i++ ){ 
    
		x = a%10; 
		sum += x;
		a /= 10;
	}
  /*takes the last digit as remainder of mod 10, then moves decimal to left \
    and repeats*/

	return sum;
}


 
