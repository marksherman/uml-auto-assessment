/************************/
/* Danny Packard        */
/* p27 Reverse          */
/* 20 minutes           */
/************************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int x[10];
  int i;
  for(i=0;i<10;i++){
  scanf("%d",&x[i]);
  }
  for(i=9;i>=0;i--){
  printf("%d\n",x[i]);
  }
  return 0;
}
