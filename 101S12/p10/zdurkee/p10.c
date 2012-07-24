/********************************************/
/*  Programmer: Zachary Durkee              */
/*                                          */
/*  Program 10: Sum of Twenty               */
/*                                          */
/*  Approximate completion time: 1 hour     */
/********************************************/

#include<stdio.h>

  int main( int argc, char *argv[] )

  {

    int i;

    int x;

    int sum;

    sum = 0;

    FILE *fin;

    fin= fopen( "testdata10", "r" );

    for( i=1; i<=20; i++ ){

      fscanf( fin, "%d", &x );

      sum=sum+x;
    
    }
      
    printf( "%d\n", sum );

    fclose( fin );

    return 0;
  
  }
