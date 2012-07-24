/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 32: Non-recursive Factorial                            */
/* Approx Completion Time: 5 minutes                              */
/******************************************************************/

#include <stdio.h>

int nonrecurse(int a);
int main( int argc, char* argv [] ){
  
  int x; 
  int y;

  printf( "Enter an integer value: " );
  scanf( "%d", &x );
  
  y=nonrecurse(x);
  printf("The factorial of the number you entered is %d\n", y);

  return 0;    
}

int nonrecurse(int a){   
  
  int i=1;
  int fact=1;

  for(i=1;i<=a;i++){
    fact=(fact*i);
  }
  
  return fact;  
}
 

