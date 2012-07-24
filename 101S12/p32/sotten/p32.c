/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 32: Non Recursive Factorial    */
/*                                       */
/*Approx. Completion Time: 25 mins       */
/*                                       */
/*****************************************/
#include<stdio.h>

int fact(int num);

int main (int agrc , char* argv[]) {
  int s;
  printf("Select an integer:\n");
  scanf("%d",&s);
  printf("The factorial value is:%d\n",fact(s));
  return 0;
}
int fact (int num){
  int m=1;
  int i;
  for (i=1 ; i<=num ; i++){
    m=m*i;
  }
  return m;
}
