/***********************************/
/*Betty Makovoz                    */
/*0ne Horizontal Line of Asterisks */
/*20 min                           */
/***********************************/

#include <stdio.h>

int main() {
  int  x, y;
  FILE *fin;

  fin = fopen("testdata8","r");

  fscanf(fin, "%d", &x);
  for(y = 0; y < x; y++){
    printf("*");
  }
  
  printf("\n");
  fclose(fin);
  
  return 0;
}
