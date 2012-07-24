/*****************************************/
/*Programmer : Min Thet Khine            */
/*                                       */
/*Program name: Using for Loop           */
/*                                       */
/*Approximate time : 20 minutes          */
/*****************************************/
#include <stdio.h>
#include <stdlib.h>
int main(void) 
{
  int testdata9;
  int i;
  FILE *fin;
 fin = fopen( "testdata9", "r");
  
  for (i= 1; i<=5; i++) {
    fscanf(fin, "%d", &testdata9);
    printf("%d \n", testdata9);
  }
  printf("\n");  
  fclose(fin);
  return 0;
}



