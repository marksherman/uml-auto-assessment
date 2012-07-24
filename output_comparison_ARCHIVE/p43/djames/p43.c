/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 43: Square Deal                     */
/*                                            */
/*Approximate completeion time: real long time*/
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int prime_check( int num );

int main(int argc, char* argv[]){  

  int N, in_val, two_N, half_N, j, x, y, z, num;

  int st_one, st_two;

  int run=1;

  int* ptr;

  printf( "Enter the number of length and height of the square\n" );

  scanf( "%d", &N );

  printf( "Enter the initial value for the center of the square\n" );

  scanf( "%d", &in_val );

  two_N = N*N;

  ptr = (int*) malloc( two_N * sizeof(int) );

  st_one = (N-1)/2;

  st_two = (N-1)/2;

  ptr[ (st_one)*N + st_two ] = in_val;

  in_val++;

  half_N = N/2;

  for( j=0; j<half_N; j++ ){
    
    for( x=0; x<run; x++ ){
      
      st_two++;

      ptr[ (st_one)*N + st_two ] = in_val;
    
      in_val++;
    }
    
    for( x=0; x<run; x++ ){

      st_one--;

      ptr[ (st_one)*N + st_two ] = in_val;

      in_val++;
    }

    run++;

    for( x=0; x<run; x++ ){

      st_two--;

      ptr[ (st_one)*N + st_two ] = in_val;

      in_val++;
    }

    for( x=0; x<run; x++ ){
      
      st_one++;
      
      ptr[ (st_one)*N + st_two] = in_val;
	   
      in_val++;
    }
    run++;
  }
  run--;
  
  for( x=0; x<run; x++ ){
    
    st_two++;
      
    ptr[ (st_one)*N + st_two ] = in_val;
      
    in_val++;
  }
  
  for( y=0; y<N; y++ ){

    for( z=0; z<N; z++ ){

      num = ptr[ y*N + z ];

      ptr[ y*N + z ] = prime_check( num );

    }
  } 
  
  for( y=0; y<N; y++ ){

    putchar( '\n' );

    for( z=0; z<N; z++ ){

      num = ptr[ y*N + z ];
      
      if( num == -1 )

	printf( "*** " );

      else

      if( num<10 )

	printf( " %d  ", num );    

      else

	printf( " %d ", num );
    }
  }

  free( ptr );

  putchar( '\n' );
    
  return 0;
}



int prime_check( int num ){

  int i;
  int x=0;

  if( num < 4 ){

    return num;

  }else{
    
    for( i=2; i<num; i++ ){    

      if( num % i == 0 )
	
	x = 1;
    }
    
    if( x==1 )
      
      return -1;
    
    else
	
      return num;
  }
}

