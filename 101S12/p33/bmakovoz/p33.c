/*******************************/
/*        Betty Makovoz        */
/*     Recursive Factorial     */
/*         20 Minutes          */
/*******************************/

# include <stdio.h>

int fact(int n);
int main (int argc, char* argv []){
  int x;
  printf("Enter a number:\n");
  scanf("%d",&x);
  printf("The factorial of the number entered is:%d\n",fact(x));
  return 0;
}

  int fact (int n){
    if(n==1){
      return 1;
    }
    else {
      return n*fact(n-1);
    }
  }
