/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Persistence of a #   */
/*                                  */
/* Completion time: 20 min          */
/************************************/

#include <stdio.h>
#include <stdlib.h>

int main ( int argc, char* argv[]) {

    int x;
    int num;

    printf(" Enter a Positive integer:\n");
    scanf("%d", &x);

    num = persistence(x);

    printf(" The Persistence of the number you entered is: %d\n", num);

    return 0;
}

int persistence( int x ) {

    int y = 0;

    while ( x > 9 ) {
	
	int z = 1;

	do {
	    z *= (x % 10);

	    x /= 10;
	}
	while( x > 0 );
	x = z;
	++y;
	
    }
    return y;
}
