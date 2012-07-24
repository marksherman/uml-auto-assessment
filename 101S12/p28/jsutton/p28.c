/********************************************/
/* Programmer: Joanna Sutton                */
/*                                          */
/* Assignment: Digit Sum                    */
/*                                          */
/* Approximate Completion Time: 45 minutes  */
/********************************************/
#include <stdio.h>

int sum(int x){
  int n=0;
  int m;

  while(x>0){
    m=x%10;
    n=m+n;
    x=x/10;
  }
  
  return n;

}
 
int main (int argc, char*argv[]){
  FILE *digits;
  int x;
  int y;

  digits=fopen(argv[1],"r");
  
  while (fscanf(digits, "%d",&x)!=EOF){
    y=sum(x);
    printf("The sum of the digits of %d is %d\n", x,y);
  }

  fclose(digits);

  return 0;
}
