/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 31: Inner product of two vectors        */
/*  Aproximate Completion time: 20 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<stdlib.h>


float inner( float u[] , float v[] , int size ) ;

int main( int argc, char *argv[] )
{

  /* Explaining program to user */
  
  printf( "I calculate the inner product of two vectors\n" ) ;

  /* Obtaining the size of the arrays below */

  int size = 0 ;
  
  printf( "Enter the size of your arrays\n" ) ;
  scanf( "%d" , &size ) ;


  int i = 0 ;
  float array1[size] ;
  float array2[size] ;

  /* Prompting user to enter their numbers into array, and then using for loop to save into memory*/

  printf( "Enter %d numbers!\n" , size ) ;
  
  for ( i=0 ; i<size ; i++ ) {
    scanf( "%f" , &array1[i] ) ;
  } 

  printf( "Enter another %d numbers!\n" , size ) ;

  for ( i=0 ; i<size ; i++ ) {
    scanf( "%f" , &array2[i] ) ;
  }
  
  /* Printing the value that the inner function returned */

  printf( "The inner product of your two vectors is = %f\n" , inner( array1 , array2 , size )) ;

  return 0 ;

}

float inner( float u[] , float  v[] , int size )
{

  float c = 0 ;
  int i = size ;

  /* For loop to multiply and then add */

  for( i=0 ; i<size ; i++ ) {
    c += u[i] * v[i];
  }

  return c ;

}
