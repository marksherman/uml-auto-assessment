/**********************************************/
/*                                            */
/* Programmer: Brian Boudreau                 */
/*                                            */
/* Assignment 7: Positive, Negative, or Zero? */
/*                                            */
/* Estimated time of Completion: 15 minutes   */
/*                                            */
/**********************************************/

#include<stdio.h>

int main(){
  int x=0;
  printf("Please enter an integer\n");
  scanf("%d",&x);
  if(x>0){
    printf("The number is positve\n");
  }
  else{
    if(x==0){
	  printf("The number is zero\n");
    }
    else{
      if(x<0){
	printf("The number is negative\n");
      }
    }
  }

  return(0);
}
