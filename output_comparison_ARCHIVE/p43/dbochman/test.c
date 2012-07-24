/*********************************************/
/* Programer: Dylan Bochman                  */
/* Program 43: Square Deal                   */
/* Time: 10 hours                            */
/*********************************************/
#include <stdio.h>
#include <stdlib.h>
int **A;
int N, initial_value;

int prime(int c);

void spiral(int h, int v, int c);

int main( int argc, char *argv[] ) {
    int i ,j, h, v, c;

    printf("Please enter array size:\n");
    scanf( "%d" , &N);

    h = N / 2; /* horizontal */
    v = N / 2; /* vertical   */

    A = malloc(N * sizeof(int *));
    for(i = 0; i < N; i++) {
	A[i] = malloc(N * sizeof(char));
    }
    
    printf("Please enter the initial integer:\n");
    scanf( "%d" , &initial_value);
    c = initial_value;


    
    for ( i = 0 ; i < (N) ; i++){
	for ( j = 0 ; j < (N); j++ ){
	    initial_value = A[i][j];
	}
    }
  
    spiral ( h, v, c );
    putchar('\n');
  
    for ( i = 0 ; i < (N) ; i++){
	for ( j = 0 ; j < (N) ; j++ ){
	    if ( (A[i][j] != -1) ){
		printf("%-5d", A[i][j] );
	    }
	    else{
		printf("%c%c%-3c" ,'*','*','*');
	    }
	}
	printf("\n");
    }
    printf("\n");
    return 0;
    
}

void spiral (int h, int v, int c){

    int k = 1;

    while (k < N) {
	int s;
	/* going right:*/
	for (s = 0; s < k; s++, h++, c++)
	    if ( ( prime ( c )== 0 ) )  {
		A[v][h]  = -1;
	    }
	    else {
		A[v][h] = c;
	    }

	/* going up:*/
	for (s = 0; s < k; s++, v--, c++)
	    if ( ( prime( c )== 0 ) ) {
			A[v][h]= -1;
		    }
		    else {
			A[v][h] = c;
		    }
			k++;
	/* going left:*/
	for (s = 0; s < k; s++, h--, c++)
	    if ( ( prime( c ) == 0 ) ){
		A[v][h]= -1;
	    }
	    else {
		A[v][h] = c;
	    }

	/* going down:*/
	for (s = 0; s < k; s++, v++, c++)
	    if ( ( prime( c )== 0 ) ){
		A[v][h]= -1;
	    }
	    else {
		A[v][h] = c;
	    }
    
	k++;
    }

    /*Final Row Going Right */
    while ( (k = N) && (h != N) ) {
	if ( ( prime ( c ) == 0  ) )  {
		    A[v][h]  = -1;
		    h++;
		    c++;
		}
		else {
		    A[v][h] = c;
		    h++;
		    c++;
		}

    }
    

    return;

}
int prime ( int c ) {
    int i;

    if ( (c == 1) )
	return 0;

    else 
        for ( i = 2 ; i < c ; i++ ) {
            if ( c % i == 0 )
		return 0;
	    }
	

    return 1;
}
