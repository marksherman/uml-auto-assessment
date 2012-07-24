/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 36: Persistence of a Number                            */
/* Approx Completion Time: 20 minutes                             */
/******************************************************************/

#include <stdio.h>

int persistence(int x);
int main( int argc, char* argv [] ){
  
  int x;
  int y;

  while(x !=EOF){
    printf("Enter an integer: ");
    scanf("%d", &x);
    if(x==EOF){
      printf("You have entered EOF...Goodbye\n");
      return 0;
    }
    else;
      y=persistence(x);
      printf("The persistence of the integer %d you entered was %d\n", x, y);
  }

  return 0;
}
 
int persistence(int x){
 
  int y=1;
  int persistence=0;
  
  do{
    y=1;
    do{
      y=y*(x%10);
      x=x/10;
    }  
    while(x>0);
      x=y;
      ++persistence;
  }
  while(y>=10);
    return persistence;
}
