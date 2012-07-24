/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 36:Persistence of a number	      */
/* 	   				      */
/* Approximate completion time : 45min        */
/**********************************************/
#include <stdio.h>

int pers(int a);
int main(int argc,char* argv[]){  
  int x, b, i;
  
  printf("Please enter the bunch number for percistence \n");
  i = 0;
  while((scanf("%d", &x))!= EOF){
    
    b = pers(x);
    printf("The persistence number is %d \n",b);
    i++;
  }
  return 0;
}
int pers(int a){
  int i,j, num, num1;
  num1 = 1;
  i = 0;
  while(a > 9){
    for(j = 0; 0 < a; j++){
      num = a % 10;
      num1 =num1* num ;
      a = a/10; 
    }
    a = num1;
    i++;
  }
  return i;
}
