/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p34: Palindrome                */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int is_palindrome( char* the_string, int start_char, int end_char );

int main(int argc, char* argv[])
{

  char x[20];
  int i;
  int str_length = 0;

  printf( "Please enter a palindrome:\n" );
  scanf( "%s", &x );

  for( i=0; i<20; i++ ){
    str_length = (x[i]==0);

  }

  if( is_palindrome(x, 0, str_length) == 1 )
    printf( "The string is a palindrome\n");
  else
    printf( "The string is NOT a palindrome\n" );

  return 0;


}
int is_palindrome( char* the_string, int start_char, int end_char ){



  if ( the_string [start_char] == the_string [end_char] )
    return 1;
  if ( the_string [start_char] != the_string [end_char] )
    return 0;
  else
    return is_palindrome( the_string, start_char+1, end_char-1 );


}
