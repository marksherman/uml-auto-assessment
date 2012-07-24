/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: scanf returns what?               */
/*                                              */
/*     Time to Completion:20 mins               */
/*                                              */
/************************************************/

#include <stdio.h>

int main( int argc,char *argv[] ){

  FILE *fin;
  int num;
  
  fin = fopen( "testdata21","r" );

  while( fscanf( fin,"%d",&num ) != EOF){

    printf( "%d\n",num );
  }

  fclose( fin );
 
  return 0;
}
