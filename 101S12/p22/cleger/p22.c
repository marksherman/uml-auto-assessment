/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Sum of a Bunch                    */
/*                                              */
/*     Time to Completion: 20 min               */
/*                                              */
/************************************************/


#include<stdio.h>


int main( int argc, char *argv[]){


  FILE *fin;

  int num = 0;
  
  int sum = 0;

  fin = fopen("testdata22","r");

  while( fscanf( fin,"%d",&num ) != EOF ){

    sum = sum + num;
  }

  fclose( fin );

  printf( "The sum is: %d\n",sum );

  return 0;
}
