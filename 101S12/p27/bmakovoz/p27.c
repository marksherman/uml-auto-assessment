/***************************/
/*       Betty Makovoz     */
/*         Reverse         */
/*        20 minutes       */
/***************************/

#include <stdio.h>

int main (int argc, char*argv[]){
  int x [10];
  int y;

  printf("Enter 10 numbers:\n");
  for(y=0 ; y<10; y++){
    scanf("%d", &x[y]);
  }
  printf("The numbers in reverse order:\n");
  for(y=9; y>=0; y--){
    printf("%d",x[y]);
  }
  printf("\n");
  return 0;
}
