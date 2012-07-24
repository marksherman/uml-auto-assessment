/************************************************/
/* Programmer: Alexander Gonzalez               */
/*                                              */
/* Assignment: Passing a two dimensional array  */
/*                                              */
/* Completion time: 30 min                      */
/************************************************/

#include <stdio.h>
#include <stdlib.h>

int sum ( int num[3][3] );

int main ( int argc, char* argv[]) {

    int num [3][3];
    int total = 0;
    
    printf("Enter 9 integers:\n");
   
    total = sum(num);

    printf("The Sum Is: %d:\n", total);

    return 0;
}

int sum( int num[3][3] ) {
    
    int x = 0;
    int r, c;

    for( r = 0; r < 3; r++) {
	
	for( c = 0; c < 3; c++) {

	    scanf("%d", &num[r][c]);

	    x += num[r][c];
	}
    }

    return x;
}
