/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Palindrome Detector              */
/*                                            */
/* Approximate completion time: 15 minutes    */
/**********************************************/

#include<stdio.h>
#include<string.h>

int is_palindrome( char* the_string, int start_char, int end_char );

int main( int argc, char *argv[] ) {
  
  char* word;
  int check, length;

  printf( "Enter a string: " );
  scanf( "%s", word );

  length = strlen( word );

  check = is_palindrome( word, 0, length ); 

  if ( check == 1 )
    printf( "Yes, it is a palindrome.\n" );
  else
    printf( "No, it is not a palindrome.\n" );

  return 0;
}

int is_palindrome( char* the_string, int start_char, int end_char ) {
  if( start_char < end_char ){
    if( the_string[start_char] == the_string[end_char - 1] )
      return is_palindrome( the_string, start_char + 1, end_char - 1 );
    else 
      return 0;
  }
  else if( start_char == end_char )
    return 1;
  else
    return 0;
}
    
