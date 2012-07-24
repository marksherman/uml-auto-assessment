/******************************************/
/*Programmer: Dalton James                */
/*                                        */
/*Program 5: Bigger than 100?             */
/*                                        */
/*Approximate completeion time: 10 minutes*/
/******************************************/

#include <stdio.h>
int main(){  
  
  int x;

  printf( "Please enter a number\n" );
  
  scanf( "%d", &x );

  if( x>100 ){

    printf( "The number is bigger than 100!\n" );
  }
 
  else{

    printf( "The number is not bigger than 100.\n" );

  }	
    return 0;
}
