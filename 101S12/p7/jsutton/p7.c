/*********************************************/
/* Programmer: Joanna Sutton                 */
/*                                           */
/* Assignment: Positive, Negative, or Zero?  */
/*                                           */
/* Approximate Completion Time: 5 minutes    */
/*********************************************/

#include <stdio.h>
int main(){
  int x;

  printf("Please enter a number:\n");
  scanf("%d", &x);
  
  if(x==0){
    printf("The number is equal to zero.\n");
  }
  else if (x>0){
    printf("The number is positive.\n");
  }
  else{
    printf("The number is negative.\n");
  }

return 0;
}
