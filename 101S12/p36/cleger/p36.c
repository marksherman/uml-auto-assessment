/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title:                                   */
/*                                              */
/*     Time to Completion:                      */
/*                                              */
/************************************************/

#include<stdio.h>

int main( int argc, char *argv[] ){

  int eof;
  int num;
  int persistence;
  int persistence_counter = 0;
  
  while(eof != EOF){
    
    printf( "enter a positive integer:" );
    eof=scanf( "%d", &num );
    
    if(eof != EOF){
      persistence = num;
      persistence_counter = 0;
      
      while( persistence>10 ){
	num = persistence;
	persistence = 1;
	while(num != 0){
	  
	  persistence *= num % 10;
	  num /= 10;
	}
	persistence_counter++;
      }
      
      printf( "the persistence is:%d\n", persistence_counter );
    }else
      putchar('\n');
    
  }
  return 0;
}
