/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Recursive Persistence             */
/*                                              */
/*     Time to Completion: 2 hours              */
/*                                              */
/************************************************/

#include<stdio.h>

int EOFcheck( int eof, int* num  );

int valuechecker_and_counter( int currentnum, int* persistence_counter );

int multiplydigits( int inputnum );

int main( int argc, char *argv[] ){

  int num;
  
  printf( "Enter a positive integer:" );

  EOFcheck( scanf( "%d", &num ), &num );

  return 0;
}

int multiplydigits( int inputnum ) {
  int multipliednumbers = 1;
  
  if(inputnum == 0)
    return 1;
  else{
    multipliednumbers *= inputnum % 10;
    inputnum /= 10;
    return( multipliednumbers*multiplydigits( inputnum ) );
  }
}

int valuechecker_and_counter( int currentnum, int* persistence_counter ){


  if(currentnum<=9){
    return 0;
  }else{
    *persistence_counter=*persistence_counter+1;
    return( valuechecker_and_counter( multiplydigits(currentnum),
				      persistence_counter));
  }
}

int EOFcheck( int eof, int* num ){

  if( eof == EOF ){
    putchar('\n');
    return 0;
  }else{
      int persistence_counter = 0;
      valuechecker_and_counter( *num, &persistence_counter );
      printf( "the persistence is:%d\n", persistence_counter );
      printf( "Enter a positive integer" );
      return( EOFcheck( scanf("%d", num) , num) );
    }
  }
  
