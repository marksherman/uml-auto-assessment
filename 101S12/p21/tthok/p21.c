/********************************************/
/* Programmer: Thearisatya Thok             */
/*                                          */
/* Program 21 : The fscanf Function         */
/*                                          */
/* Approximate completion time: 5 minutes   */
/********************************************/
#include<stdio.h>
int  main()
{
  int num;
  FILE *fin;
  fin = fopen( "testdata21", "r");
  while( fscanf( fin, "%d", &num ) != EOF ){
    printf( "%d\n", num );
  }
  fclose( fin );
  return 0;
}
