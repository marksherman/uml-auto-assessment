/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : solid box of asterisks           */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include<stdio.h>
int main(int argc, char*argv[])
{
  int L, H, i, a;  /* declare 4 variables */
  printf("Please enter 2 integers which are both lower than the number 21: \n");    /*prompts the user to put in 2 integers which are lower than 21 */
  scanf("%d %d", &L, &H);   /* reads the 2 integers from keyboard */
  for(a=1; a<=H; a++){     /* starts the for condition   */
    for(i=1;i<=L;i++){
      printf("*");  }           /*prints out the number of asteriks  */
    printf("\n"); 
  }
  return 0;
}
