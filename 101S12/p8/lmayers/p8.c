/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: One Horizontal Line of Asterisks                             */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int  n, i ;
  
  FILE * fin;
  
  fin = fopen("testdata8", "r");
  
  fscanf(fin, "%d", &n);
  
  for ( i = 0; i < n; i++){
    printf("*");
  }  
  
  printf("\n"); 
  fclose(fin);
  
  return 0;
}
