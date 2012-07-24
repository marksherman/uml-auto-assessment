/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Square Deal                       */
/*                                              */
/*     Time to Completion: 3 Hours              */
/*                                              */
/************************************************/

#include<stdio.h>
#include<stdlib.h>

int primedetector( int value );

int main( int argc, char* argv[] ){
  
  
  float size;
  int value;
  int** array;
  int i;
  int spaces = 0;
  int index1,index2;
  
  
  printf( "Enter the array size:" );
  scanf( "%f", &size );
  
  printf( "Enter an integer for the initial value:\n" );
  scanf( "%d", &value );
  
  
  
  array = (int**)malloc( size*sizeof(int*) );
  
  for( i = 0; i < size; i++ ){
    
    array[i] = (int*)malloc( size*sizeof(int) );
    
  }
  
  
  index1 = (int)((size/2)-.5);
  index2 = (int)((size/2)-.5);
  
  while( spaces+1 != size ){
    
    for( i = ++spaces; i>0; i--, index2++, value++ ){
      array[index1][index2] = primedetector( value );
    }
    for( i = spaces; i>0; i--, index1--, value++ ){
      array[index1][index2] = primedetector( value );
    }
    for( i = ++spaces; i>0; i--, index2--, value++ ){
      array[index1][index2] = primedetector( value );
    }
    for( i = spaces; i>0; i--, index1++, value++ ){
      array[index1][index2] = primedetector( value );
    }
  }
  
  for( i = spaces; i>0; i--, index2++, value++ ){
    array[index1][index2] = primedetector( value );
  }
  
  
  for( index1 = 0; index1 < size; index1++ ){
    
    for( index2 = 0; index2 < size; index2++ ){
      if( array[index1][index2] != 0 ){
	printf( "%-7d", array[index1][index2] );
      }else
	printf( "***    " );
    }
    putchar('\n');
  }

  
  for( i = 0; i < size; i++ ){
    free(array[i]);
  }
  free(array);
  
  
  return 0;
}

int primedetector( int value ){
  
  int i;
  
  for( i = value - 1; i > 1; i-- ){
    
    if( ( value % i ) == 0 )
      return 0;
    
  }
  return value;
  
}
