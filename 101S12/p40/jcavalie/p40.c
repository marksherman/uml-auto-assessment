
/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : fintite state machine */
/*Completion time:	67min	   */
/***********************************/


#include<stdio.h>
#include<stdlib.h>


int state_a = 0;
int state_b = 0;
int state_c = 0;
/*State variable for machine a = 1,b = 2, and c = 3*/

void fsm1( int input );
void fsm2( int input );
void fsm3( int input, int counter );

int main( int argc , char* argv[] ){

        int symbol;
	int i = 0;

        printf( "Enter input string\n" );

        while ( (symbol = getchar()) !=EOF ){
		if ( symbol == 48 || symbol == 49 ){
			i++;

                        fsm1( symbol );
			fsm2( symbol );
                        fsm3( symbol, i );
		}
	}


	if ( state_a == 3 )
		printf( "\tLanguage1 accepts input string\n" );
        else if ( state_a != 3)
		printf( "\tLanguage1 rejects input string\n" );


	if ( state_b == 2 )
		printf("\tLanguage2 accepts input string\n" );
	else if ( state_b != 2)
		printf( "\tLanguage2 rejects input string\n" );


	if ( state_c == 1 )
		printf( "\tLanguage3 accepts input string\n" );
        else if ( state_c != 1 )
                printf( "\tLanguage3 rejects input string\n" );


        return 0;
}

void fsm1( int input ){


        switch ( input ){

	case 48:/*48 is ascii for integer zero*/
                if ( state_a == 0){
			state_a = 1;
			break;
		}
                else if ( state_a == 1 ){
                        state_a = 1;
			break;
                }
                else if ( state_a == 2 ){
			state_a = 3;
                        break;
		}
                else if ( state_a == 3 ){
                        state_a = 3;
			break;
		}
        case 49:/*49 is ascii for integer one*/
                if ( state_a == 0){
                        state_a = 0;
                        break;
		}
                else if ( state_a == 1 ){
			state_a = 2;
                        break;
		}
                else if ( state_a == 2 ){
			state_a = 0;
                        break;
		}
                else if ( state_a == 3 ){
                        state_a = 3;
			break;
		}

	}
        return;
}



void fsm3( int input, int counter ){

	if ( counter == 1 && input != 48 )
		state_c = -1;
	/*Where -1 is a death trap state*/
	
	if ( state_c != -1 ){
                if ( counter % 2 == 0 )
			state_c = 0;
                else if( counter % 2 != 0)
                        state_c = 1;
	}

        return;
}

void fsm2( int input ){

	switch ( input ){

	case 48:/*Input is zero*/
		if ( state_b == 0 ){
			state_b = 1;
			break;
		}
		else if ( state_b == 1 ){
			state_b = 0;
			break;
		}
		else if ( state_b == 2 ){
			state_b = 3;
			break;
		}
		else if ( state_b == 3 ){
			state_b = 2;
			break;
		}
	case 49:/*Input is one*/
		if ( state_b == 0 ){
			state_b = 2;
			break;
		}
		else if ( state_b == 1 ){
			state_b = 3;
			break;
		}
		else if ( state_b == 2 ){
			state_b = 0;
			break;
		}
		else if ( state_b == 3 ){
			state_b = 1;
			break;
		}
		
	}
	return;
}
