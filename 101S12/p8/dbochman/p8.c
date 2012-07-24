/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 8:    One Horizontal Line of Asterisks       */
/* Time:         10 minutes                             */
/********************************************************/

#include <stdio.h>

int main () {

  int x , i ;

  FILE * fin ;

  fin = fopen ( "testdata8","r" ) ;

  fscanf ( fin, "%d", &x ) ;

  fclose ( fin ) ;
       for (i=0; i<x; i++){
           printf("*");
  }
       printf("\n");
    return 0 ;

  }
