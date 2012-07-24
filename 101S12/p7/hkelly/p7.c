/***************************************************/
/* Programmer Name: Harrison Kelly                 */
/*                                                 */
/* Program Name: p7 Positive, Negative, or Zero?   */
/*                                                 */
/* Approximate completion time: 10 minutes         */
/***************************************************/

#include <stdio.h>

int main(){
  
  int x;

  printf("\nEnter a number.\n");
  scanf("%d", &x);

  if( x==0 ){
    printf("\nThe number is zero.\n");
  }
  else if( x>0 ){
    printf("\nThe number is positive.\n");
  }
  else{
    printf("\nThe number is negative.\n");
  }

  return 0;
}
