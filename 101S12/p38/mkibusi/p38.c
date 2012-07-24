/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 38:Recursive Digit Sum	      */
/* 	   				      */
/* Approximate completion time : 10 min       */
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
  if(x == 0){
    return 0;
  } else
    return ((x % 10)+ digitSum(x/10));
}
