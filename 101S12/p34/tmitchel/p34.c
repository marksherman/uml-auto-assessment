/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 6: Palindrome Detector                  */
/*  Aproximate Completion time: 34 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<string.h>

int palindrome(char *s , int a , int b );

int main( int argc, char *argv[] ) {

  char word[21] ;

  printf( "Enter a word and find out if it is a palindrome!\n" );
  scanf( "%20s" , word ) ;

  int l = strlen(word) ;

  if ( l > 21 )
    printf( "The word must be between 1 and 20 letters long\n" );

  if ( palindrome( word, 0 , l) == 1 ) 
    printf( "That is a palindrome\n" );

  if ( palindrome( word , 0 , l) == 0 ) 
    printf( "That is not a palindrome\n" );

  return 0;
}

int palindrome(char *s , int a , int b) {

  if ( b <= 1 ) 
    return 1 ;

  if ( s[a] != s[b-1] ) 
    return 0 ;
  
  return palindrome(s , a+1 , b-1);

}

