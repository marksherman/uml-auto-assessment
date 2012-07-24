
/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 34: Palindrome Detector         */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char );
int main(int argc, char* argv[])
{
  char x[20];
  int str_length = 0;
  int i;

  printf( "Please enter a palindrome:\n" );
  scanf( "%s", &x );
  


  for( i=0; i<= str_length/2; i++ ){
    str_length = strlen(x);
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

