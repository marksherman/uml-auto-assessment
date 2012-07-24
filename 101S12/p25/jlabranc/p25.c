/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* Unfilled Box                    */
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>

int main(){

  int L,H,i,j;

  printf("Enter Number of Columns:");
  scanf("%d",&L);
  printf("Enter Number of Rows:");
  scanf("%d",&H);
  for(i=0;i<1;i++){
    for(j=0;j<L;j++){
      printf("*");
    }
    printf("\n");
  }
  for(i=1;i<H-1;i++){
    for(j=0;j<1;j++){
      printf("*");
    }
    for(j=1;j<L-1;j++){
      printf(" ");
    }
    for(j=L-1;j<L;j++){
      printf("*");
    }
    printf("\n");
  } 
  for(i=H-1;i<H;i++){
    for(j=0;j<L;j++){
      printf("*");
    }
    printf("\n");
  }
  return 0;
}

