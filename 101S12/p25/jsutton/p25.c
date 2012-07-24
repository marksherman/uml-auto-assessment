/********************************************/
/* Programmer: Joanna Sutton                */
/*                                          */
/* Assignment: Unfilled Box                 */
/*                                          */
/* Approximate Completion Time: 20 minutes  */
/********************************************/
#include <stdio.h>

int main(int argc, char*argv[]){
  int L;
  int H;
  int i;
  int j;
  
  printf("Please enter two integer numbers separated by a space:\n");
  scanf("%d%d",&L,&H);
  
  for(i=0;i<L;i++)
    printf("*");

  for(j=2;j<H;j++){
    printf("\n*");
    for(i=2;i<L;i++)
      printf(" ");
    printf("*");
  }

  putchar ('\n');

  for(i=0;i<L;i++)
    printf("*");

  putchar('\n');

  return 0;

}
