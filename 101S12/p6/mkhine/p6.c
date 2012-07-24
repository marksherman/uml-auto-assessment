/*******************************************************************/
/* Programmer : Min Thet Khine                                     */
/*                                                                 */
/* Program name : Equal to zero? via the scanf function            */
/*                                                                 */
/* Approximate completet time : 5 minutes                         */
#include<stdio.h>
int main(void){
  int a;
  printf("Please enter a number: ");   /* prompts the user to type a number */
  scanf("%d", &a);  /* read the input number from keyboard */
  if(a==0){
    printf("The number is equal to zero.\n"); 
  }
  else
    printf("The number is not equal to zero.\n");  /*check the value of the numb er and prints out whether the number is zero or not */
  return 0;
}
