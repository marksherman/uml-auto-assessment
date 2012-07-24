/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 26: One Dimensional Array                */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int x[10];
  int i;

  for(i = 0; i < 10; i++){
    printf("\nEnter an integer number:\n");
    scanf("%d", &x[i]);
  }

  printf("\n");

  for(i = 9; i >= 0; i--){
    printf("%d\n", x[i]);
  }

  return 0;
}
