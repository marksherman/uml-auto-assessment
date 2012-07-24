/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* Sum of a Bunch                  */
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>

int main(){
  int x,y;
  FILE *fin;
  fin = fopen("testdata22","r");
  x=0;
  y=0;
  while(fscanf(fin,"%d",&x)!=EOF){
    if(x!=EOF){
      y=y+x;
    } 
  }
  printf("%d\n",y);
  return 0;
}
