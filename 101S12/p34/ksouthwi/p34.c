/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 34: Palindrome Detector          */
/*                                          */
/* Approximate completion time: 35  minutes */
/********************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string , int start_char , int end_char );

int main( int argc , char* argv [] ) {

  char* x;
  int y = 0 , z ;

  printf( "input a string \n" );

  scanf( "%s" , x );

  z = strlen( x ) - 1 ;
  
  if( is_palindrome( x , y , z ) == 1 )

    printf( "this is a palindrome \n" );

  else
    
    printf( "this is not a palindrome \n" );
  
  return 0;

}

int is_palindrome( char* the_string , int start_char , int end_char ){

  int n = 1;

  if(  the_string[ start_char ] != the_string[ end_char ] )  
    /* base cases - automatically non-palindrome cases */
    return 0;

  else{
/* possible palindrome case - first and last chars are the same so check more*/
    start_char ++ ;
      /* redefine start and end characters for next iteration */
    end_char -- ;

    if (start_char <= end_char )/* false means it's already been checked */

      n = is_palindrome( the_string , start_char , end_char ); 
              /* recursive call */
    return n;
    
  }
  
}
