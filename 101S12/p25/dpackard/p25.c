/********************/
/* Danny Packard    */
/* p25 unfilled box */
/* Days             */
/********************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int L;
  int H;
  int i;
  int j;
  scanf("%d",&L);
  scanf("%d",&H);
  for(i=1;i<=H;i++){
    for(j=1;j<=L;j++){
      if(((i==1)||(j==1))||((i==H)||(j==L))){
	printf("*");
	}
	else{ 
	   printf(" ");
	}}
  printf("\n");
  }
  return 0;
  }
