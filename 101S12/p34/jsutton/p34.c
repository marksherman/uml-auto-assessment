/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: Palindrome Detector        */
/*                                        */
/* Approximate Completion Time: 1 hour    */
/******************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome(char* sting, int start, int end);

int main (int argc, char *argv[]){
  char string[20];
  int end;
  int pal;
  int start=0;

  printf("Please enter a string.\n");
  scanf("%s", string);

  end=strlen(string);
  end=end-1;
  
  pal=is_palindrome(string, start, end);

  if(pal==1)
    printf("The string is a palidrome.\n");
  else
    printf("The string is not a palidrome.\n");

  return 0;

}

int is_palindrome(char* string, int start, int end){
  if (string[start]==string[end]){
    if (start>=end)
      return 1;
    else return is_palindrome(string, start+1, end-1);
  }  
  else {
    return 0;
  }
}
