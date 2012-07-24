/***********************************************/
/* Programmer: Joe LaMarca                     */
/* Program: scanf returns what?                */
/* Approximate time of completion: 25 min      */
/***********************************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  int x;
  FILE* fin;
  fin=fopen("testdata21", "r");

  while(fscanf(fin,"%d",&x)!=EOF)
    printf("%d ",x);

  fclose(fin);
  printf("\n");
  
   return 0;
}
