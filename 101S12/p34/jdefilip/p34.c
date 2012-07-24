/***************************************/ 
/* Author: James DeFilippo             */ 
/* Program 34: Palindrome Detector     */ 
/* Approximate Time: 30 minutes        */ 
/***************************************/ 

#include <stdio.h>
#include <string.h>


int is_palindrome( char* the_string , int start_char , int end_char );

int main ( int argc, char* argv[] ) 
{
  char string[20]; 
  int start_letter; 
  int end_letter; 
  int x; 
  printf( "Hi! Please enter a sequence of characters for palindrome detection. " ); 
  scanf( "%s", string ); 
  start_letter = 0; 
  end_letter = strlen ( string ); 
  x = is_palindrome ( string, start_letter, end_letter ); 
  if ( x == 1 )
    printf( "The word is a palindrome.\n" ); 
  if ( x == 0 ) 
     printf( "The word is not a palindrome.\n" ); 
  return 0; 

}

int is_palindrome( char* the_string , int start_char , int end_char ) {
  
  if ((( the_string[start_char] )  == ( the_string[end_char] )) || (( the_string[start_char] ) == ( the_string[end_char - 1] )))
	return 1; 
     else 
	return 0; 
  return is_palindrome( the_string, start_char + 1, end_char - 1 );
} 
