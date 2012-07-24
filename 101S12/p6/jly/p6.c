/****************************************************************************/
/* Jennifer Ly                                                              */
/* p6.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main(){
  int x; 
  printf("Input a number and I will tell you if your number is or not equal to zero\n");

  scanf("%d", &x);
 
  if(x==0)
    printf("Your number is equal to 0\n");
  else
    printf("Your number does not equal to 0\n");
  return 0;
}
