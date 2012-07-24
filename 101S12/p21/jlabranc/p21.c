/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* scanf returns what?             */
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>

int main(){
  int x;
  FILE *fin;
  fin = fopen("testdata21","r");
  x=0;
  while(fscanf(fin,"%d",&x)!=EOF){
    printf("%d ",x);
  }
  printf("\n");
  return 0;
}

