/************************************************/
/* Programmer: Joanna Sutton                    */  
/*                                              */
/* Assignment: One Horizontal Line of Asterisks */
/*                                              */
/* Approximate Completion Time: 15 minutes      */
/************************************************/

#include <stdio.h>
int main(){

 FILE *number;
 int x;
 int i;
 
 number=fopen("testdata8","r");
 fscanf(number, "%d" ,&x);

 for(i=0; i<x; i++){
   printf("*");
 }

 printf("\n");
 fclose(number);

 return 0;
}
