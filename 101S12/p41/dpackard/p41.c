/*******************************/
/* Danny Packard               */
/* p41 malloc one dim. array   */
/* 30 minutes                  */
/*******************************/
#include<stdio.h>
#include<stdlib.h>
int main(int argc, char*argv[]){
  int x;
  int i;
  printf("enter the size of the array:");
  scanf("%d",&x);
  int *y;
  int sum=0;
  y=(int *)malloc(x*sizeof(int));
  printf("enter the array:"); 
  for(i=0;i<x;i++){
    scanf("%d",&y[i]);
    sum+=y[i];
  }
  printf("%d\n",sum);
  free(y);
  return 0;
}
