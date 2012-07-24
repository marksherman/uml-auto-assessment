/***********************/
/*   Danny Packard     */
/* p15 solid box of*'s */
/*   20 minutes        */ 
/***********************/
#include<stdio.h>
int main(int argc,char*argv[]){
  int L;
  int H;
  int x;
  int z;
  scanf("%d",&H);
  scanf("%d",&L);
  for (z=0;z<L;z++){
  for(x=0;x<H;x++){
    printf("*");
  }
  printf("\n");
  }
   return 0;
}
