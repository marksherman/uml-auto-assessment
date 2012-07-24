/*******************************/
/* Danny Packard               */
/* p32 non recursive factorial */
/* 30 minutes                  */
/*******************************/
#include<stdio.h>
int factorial(int n);
int main(int argc, char*argv[]){
  int x;
  scanf("%d",&x);
  printf("%d\n",factorial(x));
  return 0;
}
int factorial(int n){
  int y=1;
  int i;
  for(i=1;i<=n;i++){
    y=y*i;
      }
  return y;
}
