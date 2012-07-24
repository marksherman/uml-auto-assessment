/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 38: Recursive Digit Sum                                           */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int digitsum( int n );
int main( int argc, char *argv[] ) {

        int number, sum;
        FILE* fin;

        fin = fopen( argv[1], "r" );

        while( fscanf( fin, "%d", &number ) != EOF ) {
                sum = digitsum( number );
                printf( "%d\n", sum );
        }

        fclose( fin );

        return 0;

}


int digitsum( int n ) {

	if( n / 10 == 0 ) {
		return n % 10;
	}

	else {
		return n % 10 + digitsum( n / 10 );
	}
}
