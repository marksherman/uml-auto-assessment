/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 7: Positive, Negative, or Zero?     */
/*                                            */
/*Approximate completeion time: 10  minutes   */
/**********************************************/

#include <stdio.h>
int main(){  

  int x;

  printf( "enter a number\n" );

  scanf( "%d", &x);
  
  if( x==0 ){
    
    printf( "The number is zero.\n" );

  }
  else if( x>0 ){

    printf( "The number is greater than zero.\n" );

  }
  else{

    printf( "The number is less than zero.\n" );

  }
  return 0;
}
