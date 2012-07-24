/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 33: Recursive Factorial                                */
/* Approx Completion Time: 5 minutes                              */
/******************************************************************/

#include <stdio.h>

int recurse(int a);
int main( int argc, char* argv [] ){
  
  int x; 
  int y;

  printf( "Enter an integer value: " );
  scanf( "%d", &x );
  
  y=recurse(x);
  printf("The factorial of the number you entered is %d\n", y);

  return 0;    
}

int recurse(int a){   
  
  int i=0;
  int fact=1;
  
  for(i=0;i<a;i++){
    fact=fact*(a-i);
  }

  return fact;  
}
 

