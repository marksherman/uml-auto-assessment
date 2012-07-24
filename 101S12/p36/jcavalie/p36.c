/***********************************/

/*Programmer: John Cavalieri       */

/* Program : Persistence of number */

/*Completion time:      50min      */

/***********************************/



#include<stdio.h>


int persist_mapping( int num );

int main( int argc , char* argv[] ){

	int x,h,y,i = 0;

	
        do{

                i++;

		printf( "\nEnter %dth positive integer\n", i);

                if ((h = scanf( "%d" , &x )) == EOF ){

			printf( "\nEnd of Program\n");

			continue;
                }

                y = persist_mapping( x );

                printf( "\nThe persistence of %d is: %d \n" , x , y );

	} while ( h != EOF );

	return 0;
}

int persist_mapping( int num ){
       
	int temp, x, p = 0;

	while ( num > 9 ){

		x = 1;

		do{
			temp = num % 10;
			
			x *= temp;
			
			num /= 10;
		} while ( num != 0 );

	p++;
	num = x;
	}

	return p;
}
			
