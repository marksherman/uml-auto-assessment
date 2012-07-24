/******************************************************************/
/* Programmer: Jeremy Krugh                                       */
/*                                                                */
/* Program 34: Palindrome Detector                                */
/*                                                                */
/* Approximate completion time: not completed                     */
/******************************************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome(char* the_string, int start_char, int end_char);

int main(int argc, char* argv[]){

  string x;
  int y;

  scanf("%s", &x);
  y = strlen(x);

  int is_palindrome(x, 0, y-1);

  printf("Is this a palindrome? \n", is_palindrome(x, 0, y-1));

  return 0;
}

int is_palindrome(char* the_string, int start_char, int end_char){

  /* i couldnt get the function to return both a true and if the input was a palindrome. i could get it to do one or the other but not both accuratley.*?/
