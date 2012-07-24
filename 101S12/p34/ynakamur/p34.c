/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 34: Palindrome Detector                                           */
/*                                                                           */
/* Approximate completion time: 45 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char *the_string, int start_char, int end_char );

int main( int argc, char *argv[] ) {

  char *string;
  int length, result;

  printf( "\nPlease enter a string.\n" );
  
  scanf( "%s", string );

  length = strlen( string );

  result = is_palindrome( string, 0, length - 1 );

  if( result == 0 ) {
    printf( "The inputted string was NOT a palindrome.\n\n" );
  }

  else {
    printf( "The inputted string WAS a palindrome.\n\n" );
  }

  return 0;

}


int is_palindrome( char *the_string, int start_char, int end_char ) {

  char start_value, end_value;

  start_value = the_string[start_char];
  end_value = the_string[end_char];  

  if( start_value != end_value ) {
    return 0;
  }

  else if( end_char - start_char <= 1 ) {
    return 1;
  }

  else {
    return is_palindrome( the_string, start_char + 1, end_char -1 );
  }
}
