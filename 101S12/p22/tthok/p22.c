/********************************************/
/* Programmer: Thearisatya Thok             */
/*                                          */
/* Program 22 : Sum of a Bunch              */
/*                                          */
/* Approximate completion time: 5 minutes   */
/********************************************/
#include<stdio.h>
int  main()
{
  int num, sum = 0;
  FILE *fin;
  fin = fopen( "testdata22","r" );

  while( fscanf( fin, "%d", &num ) != EOF ){
    sum = sum + num;
  }
  printf( "%d\n", sum );
  fclose( fin );
  return 0;
}

