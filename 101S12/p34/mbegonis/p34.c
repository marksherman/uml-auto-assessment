/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p34                                                                               */
/*                                                                                            */
/*  This program takes a string in through standard input and computes whether or not         */
/*  it is a palindrome.  The program then returns true if the string is a palindrome          */
/*  and returns false if the string is not a palindrome.                                      */
/*                                                                                            */
/*  Approx Completion Time: 50 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>
#include <string.h>

int is_palindrome(char* the_string, int start_char, int end_char );

int main(int argc, char* argv[]){
  
  char string[21];
  int start=0, length, ans;
  
  printf("Please enter a string, and I will tell you whether or not it is a palindrome.\n");
  scanf("%s",string);
  
  length=strlen(string);                        /* Function main takes in a string of characters from the user through standard input and passes them to */
						/*   the function is_palindrome which will determine whether or not the string is a palindrome.          */
  ans=is_palindrome(string, start, length);
  
  if(ans==1){
    printf("The string is a palindrome.\n");
  }else{
    printf("The string is not a palindrome.\n");
  }
  
  return 0;
}

/* Function is_palindrome is a recursive function that computes whether or not the string is a palindrome.  */
int is_palindrome(char* the_string, int start_char, int end_char){
  
  if(start_char==end_char-1){        /* This if statement checks if the two characters equal each other, if they do the function returns 1. This should only run if the middle letter of the palindrome is reached.  */
    return 1;
  }else  if(the_string[start_char]==the_string[end_char-1]){       /* This if statement determines whether or not to proceed checking the string.  If the two characters are equal to each other,  */
    return is_palindrome(the_string, start_char+1, end_char-1);    /* then the return value will run the function is_palindrome again to see if the next two characters are equal to each other.   */
  }else if((the_string[start_char]=!the_string[end_char-1])){
    return 0;                                                      /* The final if statement will run if at any point the two characters pulled do not equal each other.  The function will then return 0.  */
  }
  
  return 0;
  
}
