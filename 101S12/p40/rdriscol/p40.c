/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: p40 FSM                */
/*                                 */
/* Approx Completion Time:3 days   */
/***********************************/


#include <stdio.h>

int lang1(int state, char input );
int lang2(int state, char input );
int lang3(int state, char input );

int main(int argc, char *argv[]){
 
	char x;
	int state1 = 0;
	int state2 = 0;
	int state3 = 0;

	printf( "Please enter any amout of zero's and ones: " );

	while( (x = getchar())!='\n' ){
				
		state1 = lang1( state1, x );
		state2 = lang2( state2, x );
		state3 = lang3( state3, x );
	}  

	if( state1 == 3 ){
		printf( "\nLanguage 1 accepts!\n" );
	}
	else
		printf( "Language 1 rejects!\n" );
		
	if( state2 == 2 ){
		printf( "Language 2 accepts!\n" );
	}
	else
		printf( "Language 2 rejects!\n" );
		
	if( state3 == 1 ){
		printf( "Language 3 accepts!\n" );
	}
	else
		printf( "Language 3 rejects!\n" );
	

	return 0; 
}
	
int lang1( int state, char input ){

	if( state == 0 && input == '1' )
		return 0;
	else if( state == 0 && input == '0' )
		return 1;
	if( state == 1 && input == '0' )
		return 1;
	else if( state == 1 && input == '1' )
		return 2;
	if( state == 2 && input == '0' )
		return 3;
	else if( state == 2 && input == '1' )
		return 0;
	if( state == 3 && input == '0' )
		return 3;
	else if( state == 3 && input == '1' )
		return 3;
 
	return 4;/* can't get here*/
}
		 
int lang2( int state, char input){

	if( state == 0 && input == '0' )
		return 1;
	else if(state == 0 && input == '1' )
		return 2;
	if( state == 1 && input == '0' )
		return 0;
	else if(state == 1 && input == '1' )
		return 3;
	if(state == 2 && input == '0' )
		return 3;
	else if(state == 2 && input == '1' )
		return 0;
	if(state == 3 && input == '0' )
		return 2;
	else if(state == 3 && input == '1' )
		return 1;
	
	return 4;/* can't get here*/
}

int lang3( int state, char input){

	if( state == 0 && input == '0')
		return 1;
	else if( state == 0 && input == '1' )
		return 1;
	if(state == 1 && input == '0' )
		return 0;
	else if( state == 1 && input == '1' )
		return 0;
	
	return 4;/* can't get here*/
}

	
	
