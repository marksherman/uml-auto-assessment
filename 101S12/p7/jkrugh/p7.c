/********************************************************************/
/* Programmer: Jeremy Krugh                                         */
/*                                                                  */
/* Program 7: Positive, Negative, or Zero?                          */
/*                                                                  */
/* Approximate Time of completion: 15 minutes                       */
/********************************************************************/

#include <stdio.h>

int main(){

int x;

scanf("%d",&x);

if (x == 0)
  printf("The number is zero\n");

if (x > 0)
  printf("The Number is positive\n");
 
if (x < 0)
   printf("The Number is negative\n");

return 0;
}
