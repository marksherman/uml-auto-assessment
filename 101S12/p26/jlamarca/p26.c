/****************************************************/
/* Programmer: Joe LaMarca                          */
/* Program: one dimensional array                   */
/* Approximate time of completion: 1 hour           */
/****************************************************/

#include <stdio.h>

int main(int agrc, char* argv[]){

  int x;
  int numbers[15];
  FILE* fin;
 
  fin=fopen("testdata26","r");

  printf("The inversed array is:");
  
  for(x=15;fscanf(fin,"%d",&numbers[x])!=EOF;x--);
  {
  for(x=1;x<=15;x++)
    printf("%d ",numbers[x]);
  }

  fclose(fin);

  printf("\n");
    
    return 0;
}
