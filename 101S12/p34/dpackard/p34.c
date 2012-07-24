/***************************/
/* Danny Packard           */
/* p34 palindrome detector */
/* 45 minutes              */
/***************************/
#include<stdio.h>
#include<string.h>
int is_palindrome(char*the_string, int start_char, int end_char);
int main(int argc, char*argv[]){
  char *string;
  scanf("%s", string);
  int i=strlen(string);
  if(is_palindrome(string,0,i-1)==1)
    printf("yes\n");
  else if(is_palindrome(string,0,i-1)==0)
    printf("no\n");
  return 0;
}
int is_palindrome(char*the_string, int start_char, int end_char){  
  while(start_char<=end_char){
    if(the_string[start_char]!=the_string[end_char])
      return 0;
    start_char++;
    end_char--;
  }
  return 1;
}
