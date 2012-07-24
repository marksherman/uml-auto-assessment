/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 38: Digit Sum with Recursion        */
/*                                            */
/*Approximate completeion time: 30 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int digitsum( int num );

int main(int argc, char* argv[]){

  FILE* fin;

  int x, y;

  fin = fopen( argv[1], "r" );

  while( fscanf( fin, "%d", &y ) != EOF ){

    x = digitsum( y );

    printf( "the sum of the individual integeres are: %d\n", x ); 
  }

  fclose( fin );
  
  return 0;
}

int digitsum( int num ){
  
  if( num == 0 ){
    
    return 0;
  
  }else{
    
    return (num%10 + digitsum( num/10 ));
  }
}

