/*****************************/
/* Danny Packard             */
/* p30 swap                  */
/* 30 minutes                */
/*****************************/
#include<stdio.h>
void swap(int*w,int*z);
int main(int argc,char*argv[]){
  int x;
  int y;
  scanf("%d",&x);
  scanf("%d",&y);
  swap(&x,&y);
  printf("%d\n",x);
  printf("%d\n",y);
  return 0;
}
void swap(int*w,int*z){
  int v;
  v=*w;
  *w=*z;
  *z=v;
  return;
}
