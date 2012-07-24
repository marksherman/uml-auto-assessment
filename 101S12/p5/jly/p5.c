/****************************************************************************/
/* Jennifer Ly                                                              */
/* p5.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main(){
  int x; 
  printf("Input a number and I will tell you if it is bigger or smaller than 100\n");

  scanf("%d", &x);
 
  if(x<100)
    printf("Your number is less than 100\n");
  
  if(x>100)
    printf("Your number is greater than 100\n");
  
  if(x==100)
    printf("Your number is 100\n");

  return 0;
}
