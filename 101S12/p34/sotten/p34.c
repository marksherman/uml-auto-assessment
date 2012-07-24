/********************************************************************/
/* Programmer: Samantha M. Otten                                    */
/*                                                                  */
/*Program 34: Palidrome Detector                                    */
/*                                                                  */
/*Approx. Completion Time: forever to understand,quick to write up  */
/*                                                                  */
/********************************************************************/

# include <stdio.h>
# include <string.h>

int is_palindrome( char*the_string, int start_char, int end_char);

int main ( int argc, char*argv[]){

  char string [20];
  int s,o;
  printf("Enter a word\n");
  scanf("%s", string);
  s = strlen(string);
  o = is_palindrome( string, 0, s-1);
  if ( o==1){
    printf("Palindrome?-WINNING!\n ");
  }
  else if (o==0) {
    printf("Palindrome?-No :/ \n");
  }
  return 0;
}

int is_palindrome ( char*the_string, int start_char, int end_char){

  if ( (the_string[start_char]) != (the_string[end_char])){
    return 0;
  }
  else if ((end_char) <= (start_char)){
    return 1;
  }
  else {
    return is_palindrome ( the_string, start_char+1, end_char-1);
  }
}
