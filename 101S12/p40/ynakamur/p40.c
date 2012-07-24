/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 40: Multiple Deterministic Finite State Machines                  */
/*                                                                           */
/* Approximate completion time: 150 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

#define STATE0 200
#define STATE1 201
#define STATE2 202
#define STATE3 203
#define REJECT_STATE 204

int count = 0;
int first_digit;

int language1( char input, int state );
int language2( char input, int state );
int language3( char input, int state );
void verifier1( int state );
void verifier2( int state );
void verifier3( void );

int main( int argc, char *argv[] ) {

	char digit;
	int result[3];

	result[0] = STATE0;
	result[1] = STATE0;
	result[2] = STATE0;

	printf( "Please enter a string of characters.\n" );

	while( ( digit = getchar( ) ) != EOF ) {
		result[0] = language1( digit, result[0] );
		result[1] = language2( digit, result[1] );
		result[2] = language3( digit, result[2] );
	}

	if( result[2] == REJECT_STATE ) {
		first_digit = 1;
	}
	else {
		first_digit = 0;
	}
     
	printf( "\n" );

	verifier1( result[0] );
	verifier2( result[1] );
	verifier3( );

	return 0;
	
}


int language1( char input, int state ) {

	int output;

	if( input == '\n' ) {
		return state;
	}

	if( state == STATE0 ) {
		if( input == '0' ) {
			output = STATE1;
		}
		else {
			output = STATE0;
		}
	}

	else if( state == STATE1 ) {
		if( input == '0' ) {
			output = STATE1;
		}
		else {
			output = STATE2;
		}
	}

	else if( state == STATE2 ) {
		if( input == '0' ) {
			output = STATE3;
		}
		else {
			output = STATE0;
		}
	}

	else {
		output = STATE3;
	}

	return output;

}


int language2( char input, int state ) {

	int output;

	if( input == '\n' ) {
		return state;
	}

	if( state == STATE0 ) {
		if( input == '0' ) {
			output = STATE1;
		}
		else {
			output = STATE2;
		}
	}

	else if( state == STATE1 ) {
		if( input == '0' ) {
			output = STATE0;
		}
		else {
			output = STATE3;
		}
	}

	else if( state == STATE2 ) {
		if( input == '0' ) {
			output = STATE3;
		}
		else {
			output = STATE0;
		}
	}

	else {
		if( input == '0' ) {
			output = STATE2;
		}
		else {
			output = STATE1;
		}
	}

	return output;

}


int language3( char input, int state ) {

	int output;

	if( input == '\n' ) {
		return state;	
	}

        if( state == STATE0 ) {
                if( input == '0' ) {
                        output = STATE1;
                        count++;
                }
                else {
                        output = REJECT_STATE;
                }
        }

	else if( state == REJECT_STATE ) {
		output = REJECT_STATE;
	}

	else {
		output = STATE1;
		count++;
	}

	return output;

}


void verifier1( int state ) {

	if( state == STATE3 ) {
		printf( "Language 1 accepts.\n" );
	}

	else {
		printf( "Language 1 rejects.\n" );
	}

	return;

}


void verifier2( int state ) {

	if( state == STATE2 ) {
		printf( "Language 2 accepts.\n" );
	}

	else {
		printf( "Language 2 rejects.\n" );
	}

	return;

}


void verifier3( void ) {

	if( ( count % 2 != 0 ) && ( first_digit == 0 ) ) {
		printf( "Language 3 accepts.\n" );
	}
	else {
		printf( "Language 3 rejects.\n" );
	}

	return;
}
