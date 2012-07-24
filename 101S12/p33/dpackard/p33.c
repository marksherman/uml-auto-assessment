/*******************************/
/* Danny Packard               */
/* p33 Factorial recursive     */
/* 5  minutes                  */
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
  if(n==0||n==1)
    return 1;
  else 
    return n*factorial(n-1);
}
  
