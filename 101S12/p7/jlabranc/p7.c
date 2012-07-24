/*Joshua LaBranche*/
/*Positive, Negative, or Zero?*/
/*20 minutes*/

#include<stdio.h>

int main(){
  int x;
  printf("Type an Integer:");
  scanf("%d",&x);
  if(x==0){
    printf("The number is zero.\n");}
  else if(x<0){
    printf("The number is negative.\n");}
  else if(x>0){
    printf("The number is positive.\n");}
  return 0;
}
