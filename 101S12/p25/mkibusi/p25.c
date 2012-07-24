/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 25: Unfilled Box		      */
/* 	   				      */
/* Approximate completion time :45 min        */
/**********************************************/

#include <stdio.h>

int main(int argc,char* argv[]){
  int j,i, x, y;
  printf("Please enter the Length and Height \n");
  scanf("%d %d",&x,&y);
  for(i = 0 ; i <= x - 1 ; i++){
    for(j = 0; j <= y - 1; j++){
      if((i > 0 && i < x-1 ) && (j > 0 && j < y-1 )){
	printf(" ");
      }
      else {
	printf("*");
      }}   
    printf("\n");
  }
  return 0;
}

