/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 32: Non-recursive factorial         */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>

int factorial( int num );

int main(int argc, char* argv[]){  

  int x;
  
  printf( "enter an integer number" );
  
  scanf( "%d", &x );

  x = factorial( x );

  printf( "The factorial of the number is: %d\n", x );

  return 0;
}

int factorial( int num ){

  int y;

  int fact=1;
  
  if( num == 0 ){
    
    return 1;

  }else{

    for( y=num; y>0; y-- ){

      fact = fact * y;
    }
    return fact;
  }
}

    
