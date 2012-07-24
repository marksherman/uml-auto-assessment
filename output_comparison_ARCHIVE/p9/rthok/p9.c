/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 09 : Using a for Loop                                          */
/*                                                                        */
/* Approximate Time: 45 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
 
  int a , b , c , d , e ;

  fin = fopen("testdata9","r");

  fscanf(fin,"%d%d%d%d%d",&a,&b,&c,&d,&e);

  fclose (fin);

  printf(" %d\n %d\n %d\n %d\n %d\n ", a , b , c , d , e );

  return 0 ;

}
