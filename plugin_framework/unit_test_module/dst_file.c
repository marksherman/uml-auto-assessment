#include "minunit.h"
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

int student_main( int argc, char *argv[] ) {
  
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
    return 1;
    }else
    return 0;
}

int tests_run = 0;
static char * test_return_number() { 
    int x; 
    x = is_palindrome("racecar", 0, 6); 
    mu_assert("racecar is not a palindrome", x == 1); 
    return 0; 
} 
static char * all_tests() {
    mu_run_test(test_return_number);
    return 0;
}
 
int main(){
    char *result = all_tests();
    if (result != 0) {
        printf("%s\n", result);
    }
    else {
        printf("ALL TESTS PASSED\n");
    }
    printf("Tests run: %d\n", tests_run);
    
    return result != 0;
 }
