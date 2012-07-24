/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 26: One Dimensional Array                */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int x[15];
  int i;

  FILE* fin;
  fin = fopen("testdata26", "r");

  for( i = 0; i < 15; i++){
    fscanf(fin, "%d", &x[i]);
  }

  fclose(fin);

  for( i = 14; i >= 0; i--){
    printf("\n%d", x[i]);
  }
  
  printf("\n");

  return 0;

}
