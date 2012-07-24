/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 22: Sum of a bunch		      */
/* 	   				      */
/* Approximate completion time :10 min        */
/**********************************************/

#include<stdio.h>

int main(int argc,char* argv[]){
  FILE *fin;
  int x , i, sum;
  fin = fopen("testdata22", "r");
  sum = 0;
  for( i = 0; (fscanf(fin,"%d",&x)) != EOF; i++){
    sum += x; 
  }
  printf("Sum of bunch of numbers = %d \n", sum);
  fclose(fin);
  return 0;
}
