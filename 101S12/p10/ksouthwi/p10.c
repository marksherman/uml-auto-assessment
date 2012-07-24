/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 10: Sum of Twenty                  */
/*                                            */
/* Approximate completion time: 10  minutes   */
/**********************************************/

#include <stdio.h>

int main ( ) {
  
  int x, i, k = 0  ;
  
  FILE *fin;
  
 fin = fopen ( "testdata10" , "r" );

 printf ( "sum =" );

 for ( i=0 ; i < 20 ; i ++ ) {
  
   fscanf ( fin ,  "%d" , &x );
   
   k = k + x ;
   
 }

 printf ( " %d  \n " , k );
 
    fclose ( fin );
  
  return 0;

}
