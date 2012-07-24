/**********************************************/
/* Programmer: Joe LaMarca                    */
/* Program: Find the Average                  */
/* Approximate time of Completion: 15 min     */
/**********************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){

  float x;
  float sum;
  float avg;
  FILE* fin;
  fin=fopen("testdata24","r");

  sum=0;
  for(x=0;fscanf(fin,"%f",&x)!=EOF;x+=1)
    sum+=x;

  avg=sum/4;

  printf("%f\n",avg);

  fclose(fin);

  return 0;
}
