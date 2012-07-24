/****************************************************/
/* Programmer: Theodore Dimitriou                   */
/* Program 34: Palindrome Detector                  */
/* Approximate completion time: 4 hours             */
/****************************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char );

int main( int argc, char* argv[] ) {
  char* the_string;
  int start_char = 0, end_char = strlen( the_string ) - 1;
  
  printf( "Enter a word: " );
  scanf( "%s", the_string );
  
  if( strlen( the_string ) <= 20 ){
    if( is_palindrome( the_string, start_char, end_char ) != 0 )
      printf( "The word %s is a palindrome.\n", the_string );
    else
      printf( "The word %s is not a palindrome.\n", the_string );
  }
  else
    printf( "The word must not exceed 20 characters.\n" );
  
  return 0;
}

int is_palindrome( char* the_string, int start_char, int end_char )
{
  if( end_char <= 1 )
    return 1;
  if( the_string[start_char] != the_string[end_char] )
    return 0;
  return is_palindrome( the_string, start_char + 1, end_char - 1 );
}
