/****************************************************/
/* Programmer Name: Harrison Kelly                  */
/*                                                  */
/* Program Name: Using a for Loop                   */
/*                                                  */
/* Approximate completion time: 15 minutes          */
/****************************************************/

#include <stdio.h>

int main(){

  FILE* fin;
  int x, y = 0;

  fin = fopen("testdata9","r");

  for ( x = 0; x < 5; x++ ){
  fscanf(fin, "%d", &y);
  printf("%d\n", y);
  }

  fclose(fin);

  return 0;
}
 
