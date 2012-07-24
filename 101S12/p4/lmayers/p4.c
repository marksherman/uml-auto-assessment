/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: The fscanf Function                                          */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int  y;
  
  FILE * fin;
  
  fin = fopen("testdata4", "r");
  
  fscanf(fin, "%d", &y);
  
  printf("The value is %d\n",y);
  
  fclose(fin);
  
  return 0;
  
}
