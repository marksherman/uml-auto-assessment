/************************************************/
/* Programmer: Joe LaMarca                      */
/* Program: sum of twenty p10                   */
/* Approximate time of completion: 20 min       */
/************************************************/

#include <stdio.h>
int main (int argc, char* argv[]){

  int x;
  int sum;
  FILE* fin;

    fin=fopen("testdata10","r");
    fscanf(fin,"%d",&x);
    fclose(fin);
    
    sum=0;
    for(x=1;x<=20;x++)
      sum+=x;

  printf("The sum of all twenty numbers is:%d\n",sum);

  return 0;
}
