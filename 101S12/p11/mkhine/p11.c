/***********************************************************/
/*Programmer : Min Thet Khine                              */
/*                                                         */
/*Program name: The abs Function                           */
/*                                                         */
/*Approximate completion time: 20 minutes                  */
/***********************************************************/
#include<stdio.h>
#include<stdlib.h>  /*header file for abs function */
int main(){
  int integer;    
  int d;
printf("Please enter a number:");
 scanf("%d", &integer);   /*reads the input from the keyboard */
 d=abs(integer);          /* returns the absolute value of integer */
printf("The absolute value of the integer is %d\n", d);

return 0;
}
