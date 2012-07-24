/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 34:Palindrome Detector             */
/* 	   				      */
/* Approximate completion time :150min        */
/**********************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome(char* the_string, int start_char, int end_char);
int main(int argc,char* argv[]){
  int i,x;
  char word[20];
  char y;
  int first, last;
  
  printf("Please enter the word ");
  
  for(i = 0; (y = getchar()) != '\n'; i++){
    
    word[i] = y;  
  }
  
  first = 0;
  last = strlen(word) - 1;
  
  x = is_palindrome(word, first, last);
  if( x == 1){
    printf("The string is palindrome  \n");
  }
  else{
    printf("The string is not palindrome \n");
  }
  return 0;
}
int is_palindrome(char* the_string, int start_char, int end_char){
  start_char = 0;;
  end_char = strlen(the_string) -1;
  
  while(start_char < end_char ) { 
    if(the_string[start_char] != the_string[end_char]){
      return 0;
    } 
    else{
      end_char --;
      start_char ++;
    }  }
  return 1;      
}

