/***************************************************/
/* Programmer: Joe LaMarca                         */
/* Program: sum of a bunch                         */
/* Approximate time of completion: 3 hours         */
/***************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int sum;
  FILE* fin;
  fin=fopen("testdata22", "r");

  sum=0;
  for(x=0;fscanf(fin,"%d",&x)!=EOF;x+=1)
    sum+=x;

  fclose(fin);

  printf("%d\n",sum);


  return 0;
}
