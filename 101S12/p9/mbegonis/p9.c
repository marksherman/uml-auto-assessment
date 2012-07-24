/*

Mike Begonis
Program p9

This program reads the variables contained in a file and prints them to the screen one at a time using fscanf.

*/


#include <stdio.h>

int main(int argc, char* argv[]){

  int a,i;
  FILE *fin;



  fin = fopen("testdata9","r");

  for(i=0;i<5;i++){
    fscanf(fin, "%d", &a);
    printf("%d\n", a);

  }

  fclose(fin);

  return 0;
}
