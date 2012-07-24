/*Programer : Min Thet Khine                      */
 /*                                                */
 /*Program name : Palindrome detector              */
 /*                                                */
 /*Approximate completion time: 1 hour             */
 /**************************************************/
 
#include<stdio.h>
#include<string.h>

int is_palindrome( char* the_string , char start_char , char end_char )
{
  int i;
  int middle;
  int j;
  i=strlen(the_string);
  middle= i/2;
  for(j=0; j<middle; j++){
    start_char= the_string[j];
    end_char= the_string[i-j-1];

    if(start_char != end_char){
      return 0;
    }
  }
  return 1;
}

int main (int argc, char ** argv)
{
  int a;
  for(a= 1; a< argc; a++){
    printf("%s is not a palindrome.\n ", argv[a]);

  }

  return 0;
}

