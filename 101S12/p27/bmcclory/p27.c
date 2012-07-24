/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #27: Reverse                                  */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int values[10];

  int i;

  printf("Type ten integer values: ");

  for(i = 0; i < 10; i++){
    scanf("%d", &values[i]);
  }
  
  for(i = 9; i >= 0; i--){
    printf("%d\n", values[i]);
  }

  return 0;
}
