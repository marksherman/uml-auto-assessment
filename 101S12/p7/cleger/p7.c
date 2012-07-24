/*****************************************/
/*                                       */
/*     Programmer: Chris Leger           */
/*                                       */
/*     Title: Positive,Negative or zero? */
/*                                       */
/*     Time to Completion: 15 mins       */
/*                                       */
/*****************************************/

#include<stdio.h>
int main(){

  int num;
  printf("Enter an Integer:");  

scanf("%d",&num);

  if(num<0){
    printf("The number is negative\n");}

  if(num>0){
    printf("The number is positive\n");}

  if(num==0){
    printf("The number is zero\n");}

  return(0);
}
