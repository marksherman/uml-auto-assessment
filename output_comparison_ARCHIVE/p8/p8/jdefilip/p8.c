/********************************************/
/* Author: James DeFilippo                  */
/* Title : One Horizontal Line of Asterisks */
/* Approximate Time: 10 minutes             */ 
/********************************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
    int x; /* creates a variable to transfer between files */
    int i; /* initialize an index variable */ 
    FILE *fin; /* direct program to location of external file */
    fin = fopen ( "testdata8", "r" ); /* specify address and deal with file permissions (make readable "r") */
    fscanf ( fin, "%d", &x ); /* select an expression using a given type */
    for ( i = 1; i <= x; ++i )
      printf( "*" );  
    fclose ( fin ); /* close the file just accessed */
    printf("\n"); /* user-friendly format */  
    return 0;
}
