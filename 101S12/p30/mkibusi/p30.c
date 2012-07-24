/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 30 :Simulating Call By Reference   */
/* 	  				      */
/* Approximate completion time : 25 min       */
/**********************************************/
#include <stdio.h>
#include <stdlib.h>

void swap(int* a, int* b); /* The purpose of this program is to swap numbers from original values*/

int main(int argc,char* argv[]){
  int x, y;
  printf("Please enter the value of x and y \n");
  scanf("%d%d",&x,&y); 
  
  printf("The number before swapping x = %d and y = %d \n", x , y);
  
  swap(&x, &y);
  
  printf("The numbers after swapping x = %d and y = %d \n", x, y);
  return 0;
}
void swap(int* a, int* b){ /* This function's code is the key of swapping values*/

  int num;   
  num = *a;
  *a = *b;
  *b = num;
  return;
}
