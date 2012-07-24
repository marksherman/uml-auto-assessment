/*******************************************************************/
/* Programmer : Min Thet Khine                                     */
/*                                                                 */
/* Program name : Bigger than 100? via the scanf function          */
/*                                                                 */
/* Approximate completet time : 20 minutes                         */
#include<stdio.h>
int main(void){
  int a;
  printf("Please enter a number: ");   /* prompts the user to type a number */
  scanf("%d", &a);  /* read the input number from keyboard */
  if(a>100){
    printf("The number is bigger than 100\n");
  }
else
  printf("The number is not bigger than 100\n");  /*check the value of the number and prints out whether the number is bigger than 100 or not */
  return 0;
}
  
