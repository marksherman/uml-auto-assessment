/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 33: Palindrome Detector                                */
/* Approx Completion Time: 50 minutes                             */
/******************************************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char);
int main( int argc, char* argv [] ){
    
  char the_string[20];
  int whatstheverdict;;
  int x=0;
  int y;

  printf("Enter String: ");
  scanf("%s", the_string);

  y=strlen(the_string)-1;  

  whatstheverdict=is_palindrome(the_string, x, y);
   
  if(whatstheverdict==0){
    printf("%s IS a palindrome \n", the_string);
  }
    else{
      printf("%s is NOT a palindrome \n", the_string);
    }

  return 0;
}
 
int is_palindrome(char* the_string, int start_char, int end_char){

/* Gives me a warning "left hand operand of comma expression has no effect"  in the for loop. After researching, It is just a syntax error when using two initializers or counters and the suggested fix is a boolean operater such as &&. However, this leaves values unused. Ive opted to stick to the comma sinceit compiles and runs fine.*/

  for(start_char, end_char; start_char<end_char && end_char>0; start_char++, end_char--){
    if(the_string[start_char] !=the_string[end_char]){
      return 1;
    } 
  }
  return 0;
}
