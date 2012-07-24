/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 37:  Digit sum		      */
/* 	   				      */
/* Approximate completion time : 60min        */
/**********************************************/

#include<stdio.h>

int digitSum(int x);
int main(int argc,char* argv[]){
  FILE* fin;
  int y, v, i;
  fin = fopen(argv[1], "r");
  
  i = 0;
  while(fscanf(fin,"%d",&v) != EOF){
    y = digitSum(v);
    i++;
    printf("The sum of bunch number is %d \n", y);
  }  
  fclose(fin);
  return 0;
}
int digitSum(int x){
  int sum = 0;
  int j = 0;
  while(x > 0){
    
    sum = sum + (x % 10);
    x = x/10;
    j++;
  }
  return sum;
}
