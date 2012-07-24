/*Joshua LaBranche*/
/*The fscanf Function*/
/*20 minutes*/

#include<stdio.h>
int main(){
  int x;
  FILE*fin;
  fin=fopen("testdata4","r");
   fscanf(fin,"%d",&x);
  fclose(fin);
  printf("testdata4 number:%d\n",x);
  return 0;
}
