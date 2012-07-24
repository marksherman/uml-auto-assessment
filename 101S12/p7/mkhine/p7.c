/*******************************************************************/
/* Programmer : Min Thet Khine                                     */
/*                                                                 */
/* Program name : Positive, Negative or 0? via the scanf function  */
/*                                                                 */
/* Approximate complete time : 10 minutes                         */
#include<stdio.h>
int main(void){
  int a;
  printf("Please enter a number: ");   /* prompts the user to type a number */
  scanf("%d", &a);  /* read the input number from keyboard */
  if(a>0){
    printf("The number is positive.\n"); /*prints the number if it's positive */
  }
  else if(a<0){
    printf("The number is negative.\n"); /*prints the number it its negative */
  }
  else if(a==0){
    printf("The number is zero. \n"); /*prints the number 0 if it's zero */
  } 
 return 0;
}
