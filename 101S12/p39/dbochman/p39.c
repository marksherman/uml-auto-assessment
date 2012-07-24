/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 39:   Persistence of a Number                */
/* Time:         20 minutes                             */
/********************************************************/

#include <stdio.h>
int carry=1, i=0;
int print ( int x );
int persist ( int x );
int main ( int argc, char *argv[] ) {

    int x ;
    printf ( "\nPlease enter a number: " ) ;
    scanf ( "%d", &x );
    if ( (x < 10) ){
	printf ("\nThe persistence of the number is 0\n");
    }
    else {
    printf ( "\nThe persistence of the number is %d\n", persist(x));
    }
    i = 0;
    print ( x );
    return 0 ;
}

int persist ( int x ) {
 
    carry *= x % 10 ;

    x /= 10 ;
    
    if ( ( x != 0 ) ) {
	persist ( x );
    }

    else {

	i++;

	x = carry;

	carry = 1;

	if ( ( x < 10 ) ) {

	    return i;

	}

	else {

	    persist ( x );

	}

	return i;
}
    return i;
}

int print (int x){
    printf ( "\nPlease enter a number: " ) ;
    scanf ( "%d", &x ) ;
    if (( x == EOF) ) {
	return 0;
    }
    else if ( x < 10 ){
	printf ( "\n The persistence of the number is 0\n\n" );
	print ( x );
    }
    else {
	printf ( "\nThe persistence of the number is %d\n\n", persist ( x\
		     )) ;
	i = 0;
	print ( x );
    }
    return 0;
}
