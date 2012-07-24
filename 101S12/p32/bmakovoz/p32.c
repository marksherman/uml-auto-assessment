/************************************/
/*          Betty Makovoz           */
/*      Non-recursive factorial     */
/*          20 Minutes              */
/************************************/

# include <stdio.h>

int fact(int n);

int main (int agrc , char* argv[]) {
  int x;
  printf("Enter a number:\n");
  scanf("%d",&x);
  printf("The factorial of the number entered is:%d\n",fact(x));
  return 0;
}


  int fact (int n){
    int f=1;
    int i; 
    for (i=1 ; i<=n ; i++){
      f=f*i;
    }
    return f;
  }
