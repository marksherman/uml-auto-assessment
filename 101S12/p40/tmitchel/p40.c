/**************************************************************/
/*  Programmer: Thomas Mitchell                               */
/*  Program 40: Multiple Deterministic Finite State Machines  */
/*  Aproximate Completion time: 2.5 hours                     */
/*                                                            */
/*                                                            */
/*                                                            */ 
/**************************************************************/


#include<stdio.h>
#include<string.h>
#include<stdlib.h>


/* Language_one functions - Step 1 through 3 */

int L_one_S_one( char *s , int a , int b ) ;

int L_one_S_two( char *s , int a , int b ) ;

int L_one_S_three( char *s , int a , int b ) ;



/* Language_two functions - step 1 through 4 */

int L_two_S_one( char *s , int a , int b ) ;

int L_two_S_two( char *s , int a , int b ) ;

int L_two_S_three( char *s , int a , int b ) ;

int L_two_S_four( char *s , int a , int b ) ;



/* Language_three functions - step 1 through 2 */

int L_three_S_one(char *s , int a , int b ) ;

int L_three_S_two( int b ) ; 



int main( int argc, char *argv[] ) {

  char word[4096] ;
  int l = 0 ;

  /* Promting user for the input of the string. */ 

  printf( "Enter a line of 0's and 1's and see if the languages accept it!\n" );
  scanf( "%s" , word ) ;

  /* Getting string length to use within functions */

  l = strlen(word) ; 

  /* True / False if statements for each language. (to print out ACCEPT or DECLINE for each language */  

  /* Language 1 if statements, calling the function, and output */

  if ( L_one_S_one( word , 0 , l ) == 1 ) 
    printf( "Language one ACCEPT!\n" ) ;
  
  if ( L_one_S_one( word , 0 , l ) == 0 )
    printf( "Language one DECLINE!\n" ) ;
 
  /* Language 2 if statements, calling the function, and output */

  if ( L_two_S_one( word , 0 , l ) == 1 )
    printf( "Language two ACCEPT!\n" ) ;

  if ( L_two_S_one( word , 0 , l ) == 0 )
    printf( "Language two DECLINE!\n" ) ;

  /* Language 3 if statements, calling the function, and output */


  if ( L_three_S_one( word , 0 , l ) == 1 )
    printf( "Language three ACCEPT!\n" ) ;

  if ( L_three_S_one( word , 0 , l ) == 0 )
    printf( "Language three DECLINE!\n" ) ;


  
  return 0;

}


int L_one_S_one( char *s , int a , int b) {

  /* If in q0 s[a] is equal to zero return a+1 to step 2 (into q1) */

  if ( s[a]-'0' == 0 )
    return L_one_S_two( s , a+1 , b ) ;

  /* Fails states */

  if ( s[b]-'0' == 1 )
    return 0 ;

  /* If in q0 s[a] is equal to 1 return a+1 back into step 1 (or q0 ) */

  if ( s[a]-'0' == 1 )
    return L_one_S_one( s , a+1 , b);

  else
    return 0 ;

}

int L_one_S_two( char *s , int a , int b ) {

  /* If in q1 s[a] is equal to 1 return a+1 to step 3 (or q2) */

  if ( s[a]-'0' == 1 )
    return L_one_S_three( s , a+1 , b ) ;

  if ( s[b]-'0' == 0 )
    return 0 ;

  /* If in q1 s[a] is equal to 0 return a+1 back into step two (or q1) */

  if ( s[a]-'0' == 0 )
    return L_one_S_two( s , a+1 , b ) ;

  else
    return 0 ;

}

int L_one_S_three( char *s , int a , int b ) {

  /* If in q2 s[a] is equal to 0 the function ACCEPTS because it enters an accept state where all numbers then are accepted */

  if ( s[a]-'0' == 0 )
    return 1 ; 
  
  /* If in q2 s[a] is equal to 1 return to step one (or q0) */ 

  if ( s[a]-'0' == 1 )
    return L_one_S_one( s , a+1 , b ) ;

  /* Other cases if the last character in the string is 0 or 1...if 0 it accepts */

  if ( s[b]-'0' == 1 )
    return 0 ; 

  if ( s[b]-'0' == 0 )
    return 1 ;

  else
    return 0 ;

}


int L_two_S_one( char *s , int a , int b ) {

  if ( s[a]-'0' == 0 )
    return L_two_S_two( s , a+1 , b ) ;

  if ( s[a]-'0' == 1 )
    return L_two_S_three( s , a+1 , b ) ;

  else
    return 0 ;

}

int L_two_S_two( char *s , int a , int b ) {

  if ( s[a]-'0' == 1 )
    return L_two_S_four( s , a+1 , b ) ; 

  if ( s[a]-'0' == 0 )
    return L_two_S_one( s , a+1 , b ) ;

  if ( s[b-1]-'0' == 1 || 0 )
    return 0 ;

  else 
    return 0 ;

}


int L_two_S_three( char *s , int a , int b ) {

  if ( s[a]-'0' == 0 )
    return L_two_S_four( s , a+1 , b ) ;

  if ( s[a]-'0' == 1 )
    return L_two_S_one( s , a+1 , b ) ;

  if ( s[b-1]-'0' == 1 )
    return 1 ;

  if ( s[b-1]-'0' == 0 )
    return 1 ;

  else 
    return 0 ;


}

int L_two_S_four( char *s , int a , int b ) {

  if ( s[a]-'0' == 0 )
    return L_two_S_three( s , a+1 , b ) ;

  if ( s[a]-'0' == 1 )
    return L_two_S_two( s , a+1 , b ) ;

  if ( s[b-1]-'0' == 1 )
    return 0 ;

  else
    return 0 ;

}


int L_three_S_one( char *s , int a , int b ) {

  /* If in q0 s[0] does not equal 0 the function fails */

  if ( s[a]-'0' == 0 )
    return L_three_S_two( b ) ;

  else
    return 0 ;

}

int L_three_S_two( int b ) {

  /* If string length (b-1) is eqaul to an odd number the function accepts */

  if ( b % 2 == 0 )
    return 0 ;

  else 
    return 1 ;

}
