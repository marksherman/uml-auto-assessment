/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 24: Find the Average                    */
/*  Aproximate Completion time: 10 minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include <stdio.h>

int main( int argc, char *argv[] )
{
    
    FILE *fin;
    float a;
    float b;
    float c;
    float d;
    float sum = 0;

    fin = fopen( "testdata24" , "r" );
    
    while( fscanf( fin , "%f%f%f%f" , &a , &b , &c , &d ) != EOF ) {
      printf( "The numbers in the file are \n%f\n%f\n%f\n%f\n" , a , b , c , d );
      sum = (a+b+c+d)/4;
      printf( "The average of the four numbers is %f\n" , sum );

    }
   
    return 0;
    
    fclose( fin );

}

