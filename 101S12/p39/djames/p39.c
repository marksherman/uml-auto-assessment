/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 39: Recursion Persistence           */
/*                                            */
/*Approximate completeion time: 100 minutes   */
/**********************************************/

#include <stdio.h>

void scanner();
int counter( int num ); 
int persistence( int num );

int main(int argc, char* argv[]){  

  scanner();

  return 0;
}

void scanner(){

  int x=0;

  int var;

  printf( "Enter a number or EOF\n" );
  
  if( scanf( "%d", &x ) != EOF ){
  
    var = counter( x );
  
    printf( "The persistence of the number is: %d\n", var );

    return scanner();
  }

  else

    printf( "EOF entered\n" );
  
  return;
}

int counter( int num ){

  if( num < 10 ){

    return 0;

  }else{

    num = persistence( num );

    return( 1 + counter( num ) );
  }
}

int persistence( int num ){
  
  int x;
  
  if( num < 10 ){
 
    return num;

  }else{
   
    x = num % 10;
    
    return( x * persistence( num/10 ));
  }
}	    

