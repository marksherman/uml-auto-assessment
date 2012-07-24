/***************************************************/
/* Programmer: Joe LaMarca                         */
/* Program: p8 Horizontal line of *                */
/* Approximate time of completion: 15 min          */
/***************************************************/

#include <stdio.h>
int main (int agrc, char* argv[]){

  int x;
  FILE* fin;

  fin=fopen("testdata8","r");
  fscanf(fin,"%d",&x);
  fclose(fin);
  
  for(x=0;x<8;x++)
    printf("*");
    
  printf("\n");

  return 0;
}
