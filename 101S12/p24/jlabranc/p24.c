/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* Find the Average                */
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>

int main(){
  int x;
  float y;
  FILE *fin;
  fin = fopen("testdata24","r");
  x=0;
  y=0;
  while(fscanf(fin,"%d",&x)!=EOF){
    if(x!=EOF){
      y=y+x;
    }
  }
    printf("%f\n",y/4);
  return 0;
}
