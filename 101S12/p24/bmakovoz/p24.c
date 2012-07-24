/***********************************/
/*          Betty Makovoz          */
/*         Find the Average        */
/*           20 minutes            */
/***********************************/

#include <stdio.h>

int main (int argc, char*argv[]){
  float a;
  float ave;
  a=0;
  ave=0;
  FILE*fin;
  fin=fopen("testdata24","r");
  while(fscanf(fin,"%f",&a)!=EOF){
    ave +=a/4;
  }
  printf("The average is:%f\n",ave);
  fclose(fin);
  return 0;
}
