/*
Mike Begonis
Project p4

This program reads a file called tesdata4 and prints the stored value inside it on a new line.


*/



#include <stdio.h>

int main(){

  int x;

  FILE *fin;
  fin = fopen("testdata4","r");
  
  fscanf(fin, "%d", &x);
 
  printf( "The number stored is %d\n",x);



  fclose(fin);



  return 0;
}
