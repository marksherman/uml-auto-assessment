/* Programmer: Rachel Driscoll    */
/*                                */
/* Title: Sum of Twenty           */
/*                                */
/* Approx Completion Time: 30 min */
/*                                */


#include <stdio.h>

int main(){
  FILE *fin;
  int i;
  int sum = 0;
  int number;
  fin=fopen("testdata10","r");

  for(i=0;i<20;i++){
    fscanf(fin,"%d",&number);
    sum = number + sum;
   
  }
  printf("The sum of the numbers equal %d\n",sum);

  fclose(fin);
  return 0;
}

  
