/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 34: Palindrome Detector                       */
/*                                                       */
/* Approximate completion time: 50 minutes               */
/*********************************************************/
#include <string.h>
#include <stdio.h>
int is_palindrome( char* the_string , int start_char , int end_char );
int main(int argc, char* argv[])
{
  char* string[20];
  int i = 0, size;
  printf("Input a word to check if it is a palindrome: ");
  /*Ask user for string input*/ 
  scanf("%s", &string);
  /*Stores inputted string into array*/
  size = strlen(string);
  /*Finds the length of inputted string*/
  if((is_palindrome(string, i, size) == 0))
    printf("The word is not a palindrome.\n");
  /*Prints not a palindrome if function is false*/
  else if((is_palindrome(string, i, size) == 1))
    printf("The word is a palindrome.\n");
  /*Prints is a palindrome if function is true*/
  return 0;
}
int is_palindrome( char* the_string , int start_char , int end_char )
{
  if(end_char <= 1)
    return 1;
  /*Once the end char reaches 1 the palindrome has checked for all match ups on both sides of word.*/
  /*Also provides case for a single character entered as a palindrome.*/
  else if(the_string[start_char] != the_string[end_char - 1])
    return 0;
  /*If the chars don't match, function will return false.*/
  else
    return is_palindrome(the_string, start_char + 1, end_char - 1);
  /*Calls function again to check the inner characters and will continue until true.*/
}
