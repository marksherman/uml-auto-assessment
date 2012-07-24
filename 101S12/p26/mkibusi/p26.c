/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 26:One Dimensional Array	      */
/* 	   				      */
/* Approximate completion time :  25min       */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc,char* argv[]){
  FILE* fin;
  int i, x;
  int num[15];
  fin = fopen("testdata26", "r");
  for(i = 0; i < 15; i++){
    fscanf(fin," %d ",&x);
    num[i] = x;
    printf("%d \n", 16 - num[i]);
    
  }
  fclose(fin);
  return 0;
}
