
/****************************************************/
/*                                                  */
/* Programmer: Josh Stone                           */
/*                                                  */
/* Program: P10 - Sum of 20                         */
/*                                                  */
/* Approx. Completion Time: 1 Hr.                   */
/*                                                  */
/****************************************************/


#include <stdio.h>

int main (int argc, char* argv [] ){

  int int1;
  int sum;
  int i;
  FILE* fin;
  

  fin = fopen("testdata10","r");

  sum = 0;

  for(i = 0 ; i <= 19 ; i ++){

    fscanf(fin,"%d",&int1);
    sum = (int1 + sum);

  }
  
  printf("%d\n",sum); 
  
  fclose(fin);

      return 0;

}


