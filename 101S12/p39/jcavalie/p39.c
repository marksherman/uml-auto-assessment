/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  recusive persistence */
/*Completion time:	50min      */
/***********************************/

#include<stdio.h>

int counter = 1;
/*Trial counter defined globally so that
 *any relevant function can increment it*/

void Per_1digit( int num  );
void mimicloop( void  );
int persist_mapping( int num , int product, int persis );

int main( int argc , char* argv[] ){
	
        
        mimicloop(  );

        printf( "\tEnd of Program\n" );

	return 0;
}

void mimicloop( void  ){


	int y,input;
        const int prodct = 1;
        const int pers = 0;

	printf( "\nEnter %dth positive integer\n", counter);

	if ( scanf( "%d" , &input ) == EOF )
                return;

        if ( input < 10 ){
		Per_1digit( input );
		return mimicloop( );
	}
		        
        y = persist_mapping( input, prodct, pers );

        printf( "\nThe persistence of %d is: %d \n" , input , y );
	counter++;
	/*Increments globally defined trial counter*/

        return mimicloop( );
}

void Per_1digit( int num ){
	const int p = 0;
	/*This function's sole purpose is to handle the persistence
	 *of of a single digit*/

	printf( "\nThe persistence of %d is: %d\n", num, p);
	counter++;
	
	return; 
}

int persist_mapping( int num , int product, int persis ){

	int temp;

	temp = num%10;
	product *= temp;
	num /= 10;

	if (num != 0)
		return persist_mapping( num, product, persis );


	else{
		persis++;
		num = product;
		product = 1;
		if ( num < 10 )
			return persis;
		else return persist_mapping( num, product, persis);
	}
}
