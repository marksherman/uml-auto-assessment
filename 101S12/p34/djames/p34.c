/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 34: Palindrome Detector             */
/*                                            */
/*Approximate completeion time: 45 minutes    */
/**********************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char );

int main(int argc, char* argv[]){  

  char string[20];

  int true_or_false;

  int length;

  printf( "enter a string for a palindrome test\n" );

  scanf( "%s", string ); 

  length = strlen( string );

  true_or_false = is_palindrome( string, 0, length-1 );

  if( true_or_false == 0 )

    printf( "The string is not a palindrome\n" );

  else
    
    printf( "The string is a palindrome\n" );

  return 0;
}

int is_palindrome( char* the_string, int start_char, int end_char ){

  if( start_char >= end_char )

    return 1;
     
  else if( the_string[start_char] != the_string[end_char] )
    
    return 0;

  else if( the_string[start_char] == the_string[end_char] )

    return is_palindrome( the_string, start_char + 1, end_char - 1 );
}
