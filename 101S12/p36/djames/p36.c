/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 36: Persistence of a Number         */
/*                                            */
/*Approximate completeion time: 40 minutes    */
/**********************************************/

#include <stdio.h>

int persistence( int num );

int main(int argc, char* argv[]){  

  int x=0;

  int var;

  printf( "Enter a number or EOF\n" );
  
  while( scanf( "%d", &x) != EOF ){
  
    var = persistence( x );
  
    printf( "The persistence of the number is: %d\n", var );

    printf( "Enter a number of EOF\n" );
  }

  return 0;
  
}
int persistence( int num ){

  int z, g;

  int count=0;

  for( g=num ; g>9; count++ ){

    for( g=1; num!=0; ){
    
      z = num%10;
      
      g = g*z;
    
      num = num / 10;
    }

    num=g;
  }
  return count;
}
