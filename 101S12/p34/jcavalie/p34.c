/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  palindrome detect	   */
/*Completion time:	15min	   */
/***********************************/

#include<stdio.h>
#include<string.h>

int is_palindrome( char* the_string , int start_char, int end_char );

int main( int argc , char* argv[] ){

	char palin[20];
	int length,y;
	const int begin = 0;
	
	printf("Enter palindrome cadidate\n" );

	scanf( "%s" , palin );

	length = strlen( palin );
  

	y = is_palindrome( palin , begin , length-1 );
 
	if ( y == 0)
		printf( " The string is not a palindrome\n " );
	else printf( "the string is a palindrome\n" );
	
	return 0;
}
  
int is_palindrome( char* the_string , int start_char, int end_char ){

	if ( the_string[start_char] != the_string[end_char] )
		return 0;
	else if ( end_char <= start_char )
		return 1;
	else
		return is_palindrome( the_string, start_char + 1, end_char - 1 );

  
}
 
