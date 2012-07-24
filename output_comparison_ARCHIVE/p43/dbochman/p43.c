/*********************************************/
/* Programer: Dylan Bochman                  */
/* Program 43: Square Deal                   */
/* Time: 1 hour                              */
/*********************************************/
#include <stdio.h>
#include <stdlib.h>
int* A;
int N, initial_value;

int prime(int initial_value);

void spiral(int h, int v, int c);

int main( int argc, char *argv[] ) {
    int i ,j, h, v, c;

    printf("Please enter an odd number between 3 and 15:\n");
    scanf( "%d" , &N);

    A =(int*) malloc( sizeof(int)* N * N);

    h = N / 2;
    v = N / 2;

    printf("Enter a starting value:\n");
    scanf( "%d" , &initial_value);
    
    A[ N * h + v] = initial_value;

    spiral( h , v , c );

    for ( i = 0 ; i < N ; i++){
	for ( j = 0 ; j < N ; j++ ){
	    if ( prime( A[ N * i + j ]) < 1 ){
                printf( "%-5d" , A[ N * i + j]);
            }
            else{
                printf( "%-4s" , "***  " );
            }
	}   
	printf( "%-1c", '\n');
	}
    free ( A );
    return 0;
    }

void spiral (h, v, c){
    
    int a = 1, b = 2, i;
	
        /* going right:*/
	for (i = 0; i  < (2 * a + 1); i++) {
	    A[ N * h + ++v ]  = ++initial_value;
	    c++;
	}
	return;
	
	/* going up: */
	for (i = 0; i  < (2 * a + 1); i++) {
	    A[ N * --h + v ]  = ++initial_value;
	    c++;
	    }
	return;
	
	/* going left: */
	for ( i = 0; i  < (2 * b); i++) {
	    A[ N * h + --v ]  = ++initial_value;
	    c++;
	}
	return;
		
	/* going down: */
	for ( i = 0; i  < (2 * b); i++) {
            A[ N * ++h + v ]  = ++initial_value;
            c++;
		}
	return;
		
		
	       	return spiral( h , v , c);
}

int prime (int initial_value) {

    int i;   
    int value = initial_value;

    if ( (value == 1) ) 
	return 0;

    else
	for ( i = 2 ; i < value ; i++ ) {
	    if ( value % i == 0 ) ;
	    return 1;
	}
    
    return 0 ;
    
}
