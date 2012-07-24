/*********************************************************************/
/* Programmer: Jeremy Krugh                                          */
/*                                                                   */
/* Program 3: Sum of Two Values                                      */
/*                                                                   */
/* Approximate completion time: 15 minutes                           */
/*********************************************************************/

#include <stdio.h>

int main(){

  int x;
  int y;
  int sum;

  printf ("Enter Two Values:");

  scanf ("%d %d",&x,&y);
  sum = x+y;

  printf  ("%d\n",sum);

  return 0;
}
