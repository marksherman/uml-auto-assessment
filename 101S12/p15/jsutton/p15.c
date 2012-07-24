/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: Solid Box of Asterisks      */
/*                                         */
/* Approximate Completion Time: 20 minutes */
/*******************************************/
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
 int L;
 int H;
 int i;
 int j;

 printf("Please enter two numbers separated by a space.\n");
 scanf("%d%d",&L,&H);
 
 for (i=0;i<H;i++){
   for (j=0;j<L;j++){
     printf("*");
   }
   printf("\n");
 }
 
 return 0;

}
