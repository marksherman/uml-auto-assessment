/********************************************************************************/
/*                                                                              */
/*  Mike Begonis                                                                */
/*  Program p24                                                                 */
/*                                                                              */
/*  This program reads 4 integer values from a file, then computes and prints   */
/*  their average to the screen as a float.                                     */
/*                                                                              */
/*  Approx Completion Time: 10 minutes                                          */
/********************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x,y=0;
  float avg;
  FILE *fin;
  
  fin = fopen("testdata24","r");
  
  while((fscanf(fin, "%d", &x))!=EOF){
    y=y+x;
  }
  
  fclose(fin);
  avg=(float)y/4;
  printf("%f\n",avg);

  return 0;
}
