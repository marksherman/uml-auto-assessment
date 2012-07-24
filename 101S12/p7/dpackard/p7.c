/***Danny Packard***/
/***p7 +,-,or 0*****/
/*** 15 minutes***/
#include <stdio.h>
int main(){
  int x;
  scanf("%d",&x);
  if((x==0))
    printf("the number is zero \n");
  if (x<0)
    printf("the number is negative \n");
  else if (x>0)
    printf("the number is positive \n");
  return 0;
}
