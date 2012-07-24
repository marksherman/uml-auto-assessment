/**********************************/
/*                                */
/*     Programmer: Chris Leger    */
/*                                */
/*     Title: Bigger than 100?    */
/*                                */
/*     Time to Completion:15 mins */
/*                                */
/**********************************/

#include<stdio.h>
int main(){
  int num;
  
  printf("enter an integer:");
  
  scanf("%d",&num);
  
  if (num>100){
    printf("The number is bigger than 100\n");
  }
  else{
    printf("The number is smaller than 100\n");
  }
  return(0);
}
