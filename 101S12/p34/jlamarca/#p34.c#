/******************************************************/
/* Programmer: Joe LaMarca                            */
/* Program: p34 palindrome detector                   */
/* Approximate time of completion:  2 hours           */
/******************************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome(char* the_string, int start_char, int end_char);

int main(int argc, char* argv[]){

  char* x = "string";
  int y;
  int z;

  x=argv[1];
  y=strlen(x);
  z=0;

  is_palindrome(x, z, y-1);

  printf("Is this a palindrome? %d \n", is_palindrome(x, z, y-1));

  return 0;
}

int is_palindrome(char* the_string, int start_char, int end_char){

  
  if(end_char<=1)
    return 1;  /* 1 means true*/
  else if(start_char==end_char)
    return(end_char - 1);
  else
    return 0; /* 0 means false */
}

/* The is_palindrome function is returning true if the string is one long but
   once it surpasses one letter, it returns false. My recursive statement is
   wrong but im not sure as to how to fix it. */
