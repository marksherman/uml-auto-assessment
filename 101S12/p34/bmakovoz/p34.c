/**********************************/
/*         Betty Makovoz          */
/*       Palindrome Detector      */
/*          60 minutes            */
/**********************************/

# include <stdio.h>

# include <string.h>
 
int is_palindrome( char*the_string, int start_char, int end_char);

int main ( int argc, char*argv[]){
  
  char string [20];
  int w,z;
  printf("Enter a word\n");
  scanf("%s", string);
  w = strlen(string);
  z = is_palindrome( string, 0, w-1);

  if ( z==1){
    printf("The word entered  is a palindrome\n ");
  }
  else if (z==0) {
      printf("The word entered is not a palindrome \n");
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
