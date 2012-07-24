/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 24:Find the average		      */
/* 	   				      */
/* Approximate completion time : 25           */
/**********************************************/
#include <stdio.h>

int main(int argc,char* argv[]){
  FILE* fin;
  int x, i; double num1, num;
  
  fin = fopen("testdata24","r");
  num = 0;
  for(i = 0; (fscanf(fin,"%d", &x)) != EOF; i++ ){
    num += x;
    num1 = num/4;
  }
  printf("The average of four number is %f \n", num1);
  return 0;
}
