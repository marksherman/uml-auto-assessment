/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Sum of Twenty                                                */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int  n, i,sum = 0 ;
  
  FILE * fin;
  
  fin = fopen("testdata10", "r");
  
  for ( i = 0; i < 20 ; i++){
    fscanf(fin, "%d", &n);     
    sum = (n + sum);
  }    
  printf("The sum is: %d\n",sum);
  
  
  fclose(fin);
  
  return 0;
}
