/*Betty Makovoz*/
/*Positive,Negative,or zero*/
/*20 min*/

#include <stdio.h>
int main(){
  int x;
  printf("Type in a number:\n");
  scanf("%d",&x);
  if ((x<0)){
    printf("The number is negative\n");
  }
  if(x>0){
    printf("The number is positive\n") ;
  }
  else if (x==0){
    printf("The number is equal to zero\n");
  }
  return 0;
} 
