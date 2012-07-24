/****************************************************************************/
/* Jennifer Ly                                                              */
/* p11.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
  int x, y;
  printf("Input an integer:\n");
  scanf("%d", &x);
  y=abs(x);
  printf("The integer's absolute value is:\n");
  printf("%d\n", y);

  return 0;
}
 
