/****************************************************/
/* Programmer: Joe LaMarca                          */
/* Program: p9, Using a for loop                    */
/* Approximate time of completion: 10 minutes       */
/****************************************************/

#include <stdio.h>
int main (int argc, char* argv[]){

  int x;
  FILE* fin;

  fin=fopen("testdata9","r");
  fscanf(fin,"%d",&x);
  fclose(fin);

  for(x=1;x<6;x++)
    printf("%d",x);

  printf("\n");

  return 0;
}
