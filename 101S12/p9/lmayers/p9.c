/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Using a for Loop                                             */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int  n, i ;
  
  FILE * fin;
  
  fin = fopen("testdata9", "r");
  
  for ( i = 0; i < 5 ; i++){
    fscanf(fin, "%d", &n); 
    printf("%d\n",n);
  }
  
  fclose(fin);
  
  return 0;
}
