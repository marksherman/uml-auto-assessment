/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 40: Multiple Deterministic Finite State Machines               */
/*                                                                        */
/* Approximate Completion Time: 360 minutes                               */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int language1 ( char input, int state );

int language2 ( char input, int state );

int language3 ( char input, int state );

int main( int argc, char *argv[] ) {

	char input = 0 ;

	int L1_state = 0 , L2_state = 0 , L3_state = 0 ;

	printf("\nEnter a string of characters followed by an EOF: ") ;

	while( ( input = getchar() ) != EOF ){

		L1_state = language1( input, L1_state ) ;

		L2_state = language2( input, L2_state ) ;

		L3_state = language3( input, L3_state ) ;
       
	}

	if( L1_state == 3 )

		printf( "\n\nLanguage 1: Accepts") ; 

 	else	

		printf( "\n\nLanguage 1: Rejects");

	if( L2_state == 2 )

		printf( "\nLanguage 2: Accepts") ; 

	else

		printf( "\nLanguage 2: Rejects") ;

	if( L3_state == 2 )

		printf( "\nLanguage 3: Accepts\n\n") ; 

	else

		printf( "\nLanguage 3: Rejects\n\n") ;  


	return 0 ;

}

int language1 ( char input, int state ){

	if( state == 0 ){

		if( input == '0' ){
				
			state = 1 ;

			return state ;
		
		}

		else{

			state = 0 ;

			return state ;

		}

	}

	if( state == 1 ){

		if( input == '0' ){ 

			state = 1 ;

			return state ;
		
		}

		else{
			
			state = 2 ;
	
			return state ;

		}

	}

	if( state == 2 ){

		if( input == '0' ){

			state = 3 ;

			return state ;

		}

		else{

			state = 0;

		return state ;

		}

	}

	if( state == 3 ){

		state = 3 ;

		return state ;

	}

	return state ;

}

int language2( char input, int state){

       	if( state == 0 ){

		if( input == '0' ){

			state = 1 ;

			return state ;

		}

		else{

			state = 2 ;

		return state ;

		}

	}

	if( state == 1){

		if( input == '0' ){

			state = 0 ;

			return state ;

		}

		else{

			state = 3 ;

			return state ;

		}

	}

	if( state == 2 ){

       		if( input == '0' ){

			state = 3 ;

			return state ;

		}

		else{

			state = 0 ;
	
			return state ;

		}

	}

	if( state == 3 ){

		if( input == '0' ){

			state = 2 ;

			return state ;

		}

		else{ 

			state = 1 ;

		return state ;

		}

	}	

	return state ;

}

int language3( char input, int state){

	if( state == 0 ){

		if( input == '0' ){

			state = 2 ;

			return state ;

		}

		else{

			state = 1 ;

			return state ;

		}

	}

	if( state == 1 ){

			state = 1 ;

			return state ;		      

	}

	if( state == 2 ){

		state = 3 ;

		return state ;

	}

	if( state == 3 ){

		state = 2 ;

		return state ;

	}

	return state ;

}
