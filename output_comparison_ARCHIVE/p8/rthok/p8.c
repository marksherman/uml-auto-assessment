/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 08 : One Horizontal Line of Asterisks                          */
/*                                                                        */
/* Approximate Time: 60 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
 
  int x , y , i ;

  fin = fopen("testdata8","r");

  fscanf(fin,"%d", &x);

  fclose (fin);

  y = putchar ('\n');

  for(i = 0; i < x; i++){
    
    printf("*");
 
  }

  y = putchar ('\n');

  y = putchar ('\n');

  return 0 ;

}
