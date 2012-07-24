/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Passing a Two Dimensional Array   */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/

#include<stdio.h>


int sum( int u[][3], int rows, int columns );

int main( int argc, char *argv[] ){
  
  int columns = 3;
  int rows = 3;
  int integers[3][3];
  int i;
  int j;
  
  printf( "Enter 9 integers seperated by spaces:" );
  
  for(i = 3; i>0; i--){
    
    for(j = 3; j>0; j--){
      scanf("%d",&integers[i-1][j-1]);
    }
  }
  
  printf( "The sum of the numbers is:%d\n", sum(integers,rows,columns) );
  
  return 0;
}

int sum( int u[][3], int rows, int columns ) {
  
  int sum1 = 0;
  int i;
  int j;
  
  for( i = rows; i>0; i-- ){
    
    for( j = columns ; j>0 ; j-- ){
      sum1 += u[i-1][j-1];
    }
    rows--;
  }
  
  return sum1;
}
