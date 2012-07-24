/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Unfilled Box                                                 */
/*                                                                       */
/* Approximate completion time: 40 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc ,char *argv[]) {
  
  int r, c, i, j;

  printf("Please enter an integer value for r and c:\n");
  scanf("%d %d", &r , &c);

  for (i = 0; i < c; i++){
    for(j = 0; j < r; j++){

    if (i == 0 || i == c - 1)printf("*");
    else
      if(j== 0 || j == r - 1)printf("*");
      else printf( " " );
  }
  putchar('\n');}

  return 0;
}
