/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 8: One Horizontal line of Asterisks     */
/*  Aproximate Completion time: 25 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] ){
    
    FILE *fin;
    int x, y;
    fin = fopen( "testdata4" , "r" );
    fscanf( fin , "%d" , &x );
    for( y = 0 ; y < x ; y++ ){
      printf( "*" );
    }
    printf( "\n" );
    fclose( fin );
   
    return 0;
}



