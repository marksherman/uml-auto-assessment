/*******************************/
/* Danny Packard               */
/* p36 persistence of a number */
/* 45 minutes                  */
/*******************************/
#include<stdio.h>
int persistence(int y);
int main(int argc, char*argv[]){
  int x;
  while(scanf("%d",&x)!=EOF){
    printf("%d\n",persistence(x));
  }
  return 0;
}
int persistence(int y){
  int v=1;
  int q=1;
  int w=1;
  while(y>9){
    do{
    v=y%10;
    q=q*v;
    y=y/10;
    }
    while(y>0);
    w++;
    y=q;
  }
  return w;
}
