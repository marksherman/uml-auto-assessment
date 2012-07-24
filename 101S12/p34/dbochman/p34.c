/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 34:   Palindrome Detector                    */
/* Time:         45 minutes                             */
/********************************************************/
#include <string.h>
#include <stdio.h>
int is_palindrome( char* the_string , int start_char , int end_char );
int main ( int argc, char *argv[] ) 
{
    int start_char, end_char,  result;
    char the_string[20];
    start_char=0;
    printf( "Please enter a potential palindrome: \n");
    scanf("%s", the_string);
    end_char = strlen ( the_string ) - 1;
    result=is_palindrome( the_string, start_char, end_char  );
	      if ( result == 1)
	      {
		  printf ( "\n Palindrome Detected \n" );
		  return 0;
	      }
	      else 
	      {
		  printf ( "\n Not a Palindrome \n" );
		  return 0;
              }
}
int is_palindrome(char* the_string , int start_char , int end_char)
	{    
	    int length;
	    length = strlen( the_string );
	    if (the_string[start_char] != the_string[end_char])
	    {
		return  0;
	    }
	    else if (the_string[start_char] == the_string[end_char])
	    {
		start_char++;
		end_char--;
		is_palindrome (the_string , start_char , end_char );
		return 1;
	    }
	    return 0;
	}


