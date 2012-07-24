/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Palindrome Dectector                                         */
/*                                                                       */
/* Approximate completion time: 2 hours                                  */
/*************************************************************************/
#include <stdio.h>
#include <string.h>

int is_palindrome ( char *String,  int start_char,  int end_char ) {
  
  if (String[start_char] != String[end_char]) return 0;
  if (start_char == end_char || start_char > end_char) return 1;
  return is_palindrome(String, start_char + 1, end_char -1 );
}

int main ( int argc, char *argv[]) {
  
  char * String;
  int sum = 0;
  
  printf("Please enter a string value:\n");
  gets( String );
  sum = is_palindrome(String, 0, strlen(String) - 1);

  if (sum == 1)printf("This string is a palindrome\n");
  else
    printf("this string is not a palindrome\n");
    return 0;
}
