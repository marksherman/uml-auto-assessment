/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 39: Recursive Persistence of a Number   */
/*  Aproximate Completion time: 2.5 hours           */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


/* NOTE: This assignment was late because of server issues, had to send a ticket into help.cs@uml.edu ( could not access my directory with all my programs */


#include<stdio.h>


int persis( int x , int add ) ;

int stepthree( int reduce , int count , int x , int add ) ;



int main( int argc, char *argv[] ) {

  int num = 0 ;
  int add = 1 ;

  printf( "Enter as many intergers as you want for this program!\n" ) ;
  printf( "Enter a number to get its persistence!\n" ) ;


  while ( scanf( "%d" , &num ) != EOF ) {


    if ( num < 10 ) { 
      printf ( "The persistence of number: %d is = 0\n" , num ) ;

    }

    else {
    printf ( "The persistence of number: %d is = %d\n" , num , persis( num , add ) ) ;
  
    }
  
  }
  
  return 0 ; 
  
}


int persis( int x , int add ) {  

  int reduce = 0 ;
  int count = 1 ;


  return stepthree( reduce , count , x , add ) ;


}
  

int stepthree( int reduce , int count , int x , int add ) {

  if ( x > 9 ) {
      
    reduce = x % 10 ;
    count = count * reduce ; 
    x = x / 10 ;
    
    return stepthree( 0 , count , x , add ) ;

  }

  else {
    count = count * x ;
    
    if ( count > 9 ) {
      add++ ;
      return persis ( count , add ) ;

    }

    else {
 
      return add ; 

    }  

  }

}
 
/* NOTE: This assignment was late because of server issues, I could not access my directory with all my programs - had to submit a support ticket */
