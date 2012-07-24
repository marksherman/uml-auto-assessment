/*************************/
/* Danny Packard         */
/* p23 fgetc and toupper */
/* 20 minutes            */
/*************************/
#include<stdio.h>
#include<ctype.h>
int main(int argc, char*argv[]){
  char x;
  FILE*fin;
  fin=fopen("testdata23","r");
  while((x=fgetc(fin))!=EOF){
  putchar (toupper(x));
  continue;
  }
  fclose(fin);
  return 0;
}
