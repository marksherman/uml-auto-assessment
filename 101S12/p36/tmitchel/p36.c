/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 36: Persistence of a Number             */
/*  Aproximate Completion time: 40 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>


int persis( int x ) ;

int main( int argc, char *argv[] ) {

  int num ;

  printf( "Enter as many intergers as you want!\n" ) ;

  /* Scanning for num until !=EOF */

  while ( scanf( "%d" , &num ) != EOF ) {

    /* Calling persis( int x ) within printf to output answer */

    printf ( "The persistence of number: %d is = %d\n" , num , persis( num ) ) ;

  }

  return 0 ; 

}


int persis( int x ) {  

  int reduce = 0 ;
  int count = 1 ;
  int add = 0 ;

  /* Using Do while loop in another while loop */

  while( x > 9 ) {
    count = 1 ;
    
    do{  

      /* % 10 to get last digit , divide by ten to then get the next digit within x */

      reduce = x % 10 ;
      count = count * reduce ; 
      x = x / 10 ;
    
    }
    
    while ( x > 0 ) ; 

    /* add++ is adding how many times loop has succeeded - which the fuction reurns */

    add++ ;
    x = count ;

  }
  
  return ( add ) ;

}

