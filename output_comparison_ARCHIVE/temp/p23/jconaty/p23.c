/***********************/
/*Jake Conaty          */
/*projedt 23           */
/*apx time: 15 min     */
/***********************/

#include <stdio.h>
#include <ctype.h>

int main(int argc, char* argv[]){

  int x;

  FILE *fin;
  fin=fopen("testdata23", "r");

  while((x=toupper(fgetc(fin)))!=EOF){
    putchar(x);
  }

  fclose(fin);

  return 0;
}
