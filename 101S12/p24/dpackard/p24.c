/*********************/
/* Danny Packard     */
/* p24 Average       */
/* 20 minutes        */
/*********************/
#include<stdio.h>
#include<math.h>
int main(int argc, char*argv[]){
  int x;
  float sum=0;
  int i;
  FILE*fin;
  fin=fopen("testdata24","r");
  for(i=0;i<4;i++){
    fscanf(fin, "%d", &x);
    sum += x;
  }
  printf("%f\n",sum/4);
  fclose(fin);
  return 0;
}
