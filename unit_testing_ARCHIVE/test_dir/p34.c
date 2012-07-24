/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Palindrome Detector               */
/*                                              */
/*     Time to Completion: 1 Hour               */
/*                                              */
/************************************************/

#include<stdio.h>
#include<string.h>

int is_palindrome( char* the_string , int start_char , int end_char );

int main( int argc, char *argv[] ) {
  
  int answer;
  char string[21];
  
  printf( "Enter the String you want palindrome detection on:" );
  scanf( "%s", string );
  
  answer = is_palindrome( string , 0 , (int)strlen(string)-1 );
  
  if( answer == 1 )
    printf( "The word is a panindrome\n" );
  else
    printf( "The word is not a palindrome\n" );
  
  return 0;
}

int is_palindrome( char* the_string , int start_char , int end_char ) {
  if( the_string[start_char] == the_string[end_char] ) {
    
    if( (end_char-start_char==1)||(end_char-start_char==2) )
      return 1;
    return is_palindrome( the_string, start_char+1, end_char-1 );
  }else
    return 0;
}
