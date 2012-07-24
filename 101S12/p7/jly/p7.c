/****************************************************************************/
/* Jennifer Ly                                                              */
/* p7.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main(){
  int x; 
  printf("Input a number and I will tell you if your number positive, zero, or negative\n");

  scanf("%d", &x);
 
  if(x>0)
    printf("Your number is positive\n");
  if(x==0)
    printf("Your number is zero\n");
  if(x<0)
    printf("Your number is negative\n");

return 0;

}

