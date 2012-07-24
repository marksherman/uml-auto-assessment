/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Palindrome Detector                                          */
/*                                                                           */
/* Approximate Completion Time : 30 minutes                                  */
/*****************************************************************************/


#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string , int start_char , int end_char ) ;

int main( int argc , char* argv[] ) {
	
	char string[20] ;
	int start = 0 , end , TorF ;

	printf( "Enter a string that may be a palindrome\n" ) ;
	scanf( "%s" , string ) ;
	end = strlen(string) - 1 ;
	/* TorF = True or False */
	TorF = is_palindrome( string , start , end ) ;
	if( TorF == 1 ) 
		printf( "String is a Palindrome!!!\n" ) ;
	else 
		printf( "String is not a Palindrome.\n" ) ;
	return 0 ;

}

int is_palindrome( char* the_string , int start_char , int end_char ) {
	
	/* base case, if string is only 1 letter long */
	if ( end_char <= 1 ) 
		return 1 ;
	/* test if outside letters are the same */
	if ( the_string[start_char] != the_string[end_char] )
		return 0 ;
	/* recursion!!! */
	return is_palindrome( the_string , start_char + 1 , end_char - 1 ) ; 
}
