/********************/
/*Betty Makovoz     */
/*Using a for Loop  */
/* 25 min           */
/********************/       

#include <stdio.h>

int main() { 
  int x, i;
  FILE *fin;
  fin = fopen("testdata9", "r");
  for (i = 0; i < 5; i++) {
    fscanf(fin,"%d",&x);
    printf("%d\n", x);
  }    
  fclose(fin);
  return 0;
}
