/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #34: Palidrome Detector                       */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>
#include <string.h>

char palidromeDetector(char string, int i, int j); /* i = start and j = end */

  int main(int argc, char* argv[]){

  char string;

  int i = 0, j;

  printf("Type a string: ");
  scanf("%s", &string);

  j = strlen(string);

  char palidromeDetector(char string, int i, int j);

  return 0;
}

char palidromeDetector(char string, int i, int j){

  i = strlen(string);

  j = strlen(string);

  int length = strlen(string);

  if(string[i] != string[j]){
    return false;
  }
  else if(length <= 1){
    return true;
  }
  else{
    palidromeDetector(palidromeDetector(string));
  }
}
