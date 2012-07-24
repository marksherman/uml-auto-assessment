/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 24:Find the Average     */
/*                                */
/*Approx. Completion Time: 20mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  float a, mean;

  a=0;

  mean=0;

  FILE*fin;

  fin=fopen("testdata24","r"); /*opens and reads contents of file testdata24*/

  while(fscanf(fin, "%f", &a)!=EOF){
    
    mean+=a/4;

  }

  printf("The average is:%f\n",mean);

  fclose(fin);

  return 0;
}
