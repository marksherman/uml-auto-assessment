/************************************************/
/* Programmer: Kyle White                       */
/* Program  34: Palindrome Detector             */
/* Approximate completion time:                 */
/*                                              */
/************************************************/


#include <stdio.h>
#include <string.h>

int is_palindrome( char * the_string , int start_char , int end_char );
int main (int argc, char* argv [])
  
{

  int y=0,length=0;
  char x[20];

  printf( "Enter a word to see if it is a palindrome: " );

  scanf( "%s", x );

  length = strlen ( x );

  y = is_palindrome( x , 0 , length-1 );

  if ( y == 1 )

    printf( "\nThe word is a palindrome!\n\n" );

  else

    printf( "\nThe word is not a palindrome!\n\n" );

  return 0;

}

int is_palindrome( char* the_string , int start_char , int end_char )

{

  if (( start_char > end_char ) || ( start_char == end_char ))

    return 1;

  else if ( the_string[start_char] != the_string[end_char] )

    return 0;

  else

    return is_palindrome( the_string , the_string[start_char+1] , the_string[end_char-1] );

}
