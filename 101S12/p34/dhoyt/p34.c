/********************************/
/* Programmer: David Hoyt       */
/* Program: Palindrome Det      */
/* Time: 2 hours                */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char );

int main(){

  char* word;

  int x, len;

  printf( "Enter a string, up to twenty (20) characters:" );

  scanf( "%s", word );

  len = strlen( word );

  x = is_palindrome( word, 0, len-1 );

  if( x==1 )

  printf( "True\n" );

  else

    printf( "False\n" );
  
  return 0;

}

int is_palindrome( char* the_string, int start_char, int end_char ){

  if( ((the_string[start_char]) != (the_string[end_char])) )

    return 0;

  else if( (start_char==end_char) )

    return 1;

  else 

    return is_palindrome( the_string, start_char+1, end_char-1 );

}
