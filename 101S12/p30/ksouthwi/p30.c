/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 30: Simulating Call By Reference    */
/*                                             */
/* Approximate completion time: 20  minutes    */
/***********************************************/

#include <stdio.h>

void swap ( int *a , int *b );

int main( int argc , char* argv[] ) {

  int a , b ;

  printf( "Input two integers.\n" );

  scanf( "%d %d" , &a , &b );

  swap(  &a , &b ); /*takes two addresses as inputs*/

  printf( "%d %d \n" , a , b );

  return 0;

}

void swap ( int *a , int *b ){
  
  int temp;

  temp = *a; 
/*what's in where a points to is stored into temp - aka main's a is stored into temp*/
  *a = *b; 
/*what's in what b points to is stored in what a points to- aka main's b is put in main's a*/
  *b = temp;
/*what's in temp is not stored in what b points to- aka into main's b*/

  return ;
}
