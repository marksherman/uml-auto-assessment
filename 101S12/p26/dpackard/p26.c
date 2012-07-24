/************************/
/* Danny Packard        */
/* p26 one dim. array   */
/* 20 minutes           */
/************************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int x[15];
  int i;
  FILE *fin;
  fin=fopen("testdata26","r");
  for (i=1;i<15;i++){
    fscanf(fin,"%d", &x[i]);
  }
  for(i=14;i>0;i--){
    printf("%d\n",x[i]);
  }
  fclose (fin);
  return 0;
}
